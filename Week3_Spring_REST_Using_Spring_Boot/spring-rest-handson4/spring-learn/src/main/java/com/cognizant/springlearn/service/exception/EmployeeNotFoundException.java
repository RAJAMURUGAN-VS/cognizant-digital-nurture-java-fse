package com.cognizant.springlearn.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * EmployeeNotFoundException — HTTP 404 when an employee id is not found.
 *
 * @ResponseStatus maps this exception to HTTP 404 Not Found.
 * Spring MVC returns:
 * {
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "Employee not found"
 * }
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Employee not found")
public class EmployeeNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public EmployeeNotFoundException(int id) {
        super("Employee not found for id: " + id);
    }
}
