package com.yorku.exception;

import com.yorku.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * Global Exception Handler
 * York U Academic Admin Portal
 *
 * Description:
 * Centralized exception handling to ensure standardized JSON responses
 * and secure error logging.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. Handle Custom Business Logic Exceptions
     * (e.g., "Cannot delete department with active employees")
     */
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        log.warn("Business Logic Error: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 2. Handle Database Constraint Violations
     * (e.g., Duplicate Username)
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("Database Integrity Violation:", e);

        String message = e.getMessage();
        // Safer parsing logic to prevent IndexOutOfBoundsException
        if (message != null && message.contains("Duplicate entry")) {
            try {
                int duplicateEntryIndex = message.indexOf("Duplicate entry");
                String errMsg = message.substring(duplicateEntryIndex);
                String[] split = errMsg.split(" ");

                // Ensure the array has enough elements before accessing index 2
                if (split.length > 2) {
                    String duplicateValue = split[2];
                    return Result.error("Action Failed: The record " + duplicateValue + " already exists.");
                }
            } catch (Exception parseEx) {
                log.error("Error parsing duplicate key message", parseEx);
            }
        }

        return Result.error("Database Error: Duplicate record found.");
    }

    /**
     * 3. Handle Input Validation Errors (@Valid / @NotNull)
     * This is crucial for RESTful APIs.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidationException(MethodArgumentNotValidException e) {
        // Get the specific validation error message (e.g., "Username cannot be empty")
        String errorMsg = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        log.warn("Input Validation Failed: {}", errorMsg);
        return Result.error("Validation Error: " + errorMsg);
    }

    /**
     * 4. Handle JSON Parsing Errors
     * (This aligns with your resume about "JsonParseException handling")
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleJsonException(HttpMessageNotReadableException e) {
        log.error("JSON Parse Error:", e);
        return Result.error("Invalid Request Format: Please check your JSON syntax.");
    }

    /**
     * 5. Catch-All System Exception (Last Resort)
     */
    @ExceptionHandler(Exception.class)
    public Result handleGenericException(Exception e) {
        // Log full stack trace for debugging
        log.error("Unhandled System Exception:", e);

        // Return generic message to user for security
        return Result.error("System Error. Please contact IT Helpdesk.");
    }
}