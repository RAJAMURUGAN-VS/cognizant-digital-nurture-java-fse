package com.library.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * LoggingAspect — cross-cutting concern for method-level logging.
 *
 * Exercise 3 : @Around advice measures and logs method execution time.
 * Exercise 8 : @Before and @After advice log entry/exit for all
 *              methods in com.library.service and com.library.repository.
 *
 * Pointcut expressions used:
 *   execution(* com.library.service.*.*(..))    — all methods in service layer
 *   execution(* com.library.repository.*.*(..)) — all methods in repository layer
 *   execution(* com.library..*.*(..))           — all methods anywhere under com.library
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // ---------------------------------------------------------------
    // Exercise 8: @Before advice — logs method entry
    // ---------------------------------------------------------------
    /**
     * Runs BEFORE any method in com.library.service package.
     */
    @Before("execution(* com.library.service.*.*(..))")
    public void logBeforeServiceMethod(JoinPoint joinPoint) {
        logger.info("[BEFORE] Entering method: {}.{}()",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName());
    }

    /**
     * Runs BEFORE any method in com.library.repository package.
     */
    @Before("execution(* com.library.repository.*.*(..))")
    public void logBeforeRepositoryMethod(JoinPoint joinPoint) {
        logger.info("[BEFORE] Entering repository method: {}.{}()",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName());
    }

    // ---------------------------------------------------------------
    // Exercise 8: @After advice — logs method exit
    // ---------------------------------------------------------------
    /**
     * Runs AFTER any method in com.library.service package
     * (runs whether the method succeeds or throws).
     */
    @After("execution(* com.library.service.*.*(..))")
    public void logAfterServiceMethod(JoinPoint joinPoint) {
        logger.info("[AFTER]  Exiting  method: {}.{}()",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName());
    }

    /**
     * Runs AFTER any method in com.library.repository package.
     */
    @After("execution(* com.library.repository.*.*(..))")
    public void logAfterRepositoryMethod(JoinPoint joinPoint) {
        logger.info("[AFTER]  Exiting  repository method: {}.{}()",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName());
    }

    // ---------------------------------------------------------------
    // Exercise 3: @Around advice — measures execution time
    // ---------------------------------------------------------------
    /**
     * Wraps ALL methods under com.library (service + repository).
     * Measures and logs elapsed time in milliseconds.
     *
     * @Around intercepts the method call; we must call proceed()
     * to actually invoke the real method.
     */
    @Around("execution(* com.library..*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String className  = proceedingJoinPoint.getTarget().getClass().getSimpleName();
        String methodName = proceedingJoinPoint.getSignature().getName();

        long startTime = System.currentTimeMillis();
        logger.debug("[AROUND] Starting: {}.{}()", className, methodName);

        Object result;
        try {
            // Proceed with the actual method invocation
            result = proceedingJoinPoint.proceed();
        } catch (Throwable ex) {
            long elapsed = System.currentTimeMillis() - startTime;
            logger.error("[AROUND] {}.{}() threw {} after {}ms",
                    className, methodName, ex.getClass().getSimpleName(), elapsed);
            throw ex;
        }

        long elapsed = System.currentTimeMillis() - startTime;
        logger.info("[AROUND] {}.{}() completed in {}ms", className, methodName, elapsed);
        return result;
    }
}
