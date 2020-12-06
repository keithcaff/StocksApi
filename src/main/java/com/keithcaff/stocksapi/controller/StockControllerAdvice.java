package com.keithcaff.stocksapi.controller;

import com.keithcaff.stocksapi.exception.ApiError;
import com.keithcaff.stocksapi.exception.StockConflictException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice(assignableTypes = StockController.class)
public class StockControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(StockConflictException.class)
    public ResponseEntity<ApiError> stockConflictException(final StockConflictException e) {
        final ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, e.getLocalizedMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ApiError> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
        log.error(ex.getLocalizedMessage());

        final List<String> errors = new ArrayList<>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
        }

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
