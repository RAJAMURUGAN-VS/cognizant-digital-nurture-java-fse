package com.cognizant.springlearn;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

/**
 * GlobalExceptionHandler — centralized exception handling for all controllers.
 *
 * @ControllerAdvice — intercepts exceptions thrown from any @RestController.
 *   Without this, we would need try-catch in every controller method.
 *
 * Extends ResponseEntityExceptionHandler:
 *   Provides default handling for Spring MVC exceptions.
 *   We override methods to customise the response body.
 *
 * Handles:
 *   1. MethodArgumentNotValidException — @Valid validation failures
 *      Triggered when @RequestBody @Valid fails constraint checks.
 *      Returns 400 Bad Request with list of validation errors.
 *
 *   2. HttpMessageNotReadableException — malformed JSON / wrong field types
 *      Triggered when Jackson cannot parse the request body.
 *      E.g. sending a string where a number is expected,
 *      or wrong date format for @JsonFormat field.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ---------------------------------------------------------------
    // Handler 1: @Valid validation failures
    // ---------------------------------------------------------------

    /**
     * Called when @Valid on @RequestBody finds constraint violations.
     *
     * Flow:
     *   POST /countries with {"code":"I","name":"India"}
     *   → @Valid on Country checks @Size(min=2,max=2) on code
     *   → code "I" has length 1 → violation
     *   → Spring throws MethodArgumentNotValidException
     *   → THIS method is called (NOT the controller method)
     *
     * Proof: Check logs — CountryController logs will NOT appear,
     * but GlobalExceptionHandler START log WILL appear.
     * This confirms the controller was bypassed.
     *
     * Response:
     * {
     *   "timestamp": "...",
     *   "status": 400,
     *   "errors": ["Country code should be 2 characters"]
     * }
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        LOGGER.info("START handleMethodArgumentNotValid");

        // Build response body map
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        // Extract all field error messages from the BindingResult
        // ex.getBindingResult() contains all constraint violations
        // getFieldErrors() returns the list of per-field errors
        // x.getDefaultMessage() returns the message from the annotation
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);

        LOGGER.debug("Validation errors: {}", errors);
        LOGGER.info("END handleMethodArgumentNotValid");

        return new ResponseEntity<>(body, headers, status);
    }

    // ---------------------------------------------------------------
    // Handler 2: Malformed JSON / incorrect field type
    // ---------------------------------------------------------------

    /**
     * Called when Jackson cannot deserialise the request body.
     *
     * Triggered by:
     *   a) Sending a string where a number is expected:
     *      {"id":"abc","name":"Alice"} → id should be int, not string
     *   b) Wrong date format:
     *      {"dateOfBirth":"1990-03-15"} → @JsonFormat expects "dd/MM/yyyy"
     *
     * InvalidFormatException (cause of HttpMessageNotReadableException):
     *   getPath() returns the list of JSON path references that caused the error.
     *   reference.getFieldName() gives the field name with the wrong type.
     *
     * Response:
     * {
     *   "timestamp": "...",
     *   "status": 400,
     *   "error": "Bad Request",
     *   "message": "Incorrect format for field 'id'"
     * }
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        LOGGER.info("START handleHttpMessageNotReadable");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", "Bad Request");

        // Check if the cause is an InvalidFormatException (wrong type/format)
        if (ex.getCause() instanceof InvalidFormatException) {
            final Throwable cause = ex.getCause();
            for (InvalidFormatException.Reference reference :
                    ((InvalidFormatException) cause).getPath()) {
                body.put("message",
                        "Incorrect format for field '" + reference.getFieldName() + "'");
            }
        } else {
            body.put("message", "Malformed JSON request");
        }

        LOGGER.debug("Message not readable: {}", body.get("message"));
        LOGGER.info("END handleHttpMessageNotReadable");

        return new ResponseEntity<>(body, headers, status);
    }
}
