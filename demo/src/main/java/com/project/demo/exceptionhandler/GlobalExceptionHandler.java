package com.project.demo.exceptionhandler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value={ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleException(ConstraintViolationException error) {

        HashMap<String,String> errorMap = new HashMap<>();
        error.getConstraintViolations().forEach((constraintViolation) -> {
            String fieldName = constraintViolation.getPropertyPath().toString();
            String errorMessage = constraintViolation.getMessage();
            errorMap.put(fieldName,errorMessage);
        });

        return errorMap;
    }

    @ExceptionHandler(value={DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleException(DataIntegrityViolationException error) {

        HashMap<String,String> errorMap = new HashMap<>();
        String errorMessage = error.getMessage();
        errorMap.put("DataIntegrityViolationException", errorMessage);
        return errorMap;
    }

    @ExceptionHandler(value={ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleException(ResourceNotFoundException error) {

        HashMap<String,String> errorMap = new HashMap<>();
        String errorMessage = error.getMessage();
        errorMap.put("Resource not found error", errorMessage);
        return errorMap;
    }

}
