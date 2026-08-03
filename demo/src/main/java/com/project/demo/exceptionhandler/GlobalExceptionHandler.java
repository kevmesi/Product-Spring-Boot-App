package com.project.demo.exceptionhandler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value={MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleException(MethodArgumentNotValidException error) {

        HashMap<String,String> errorMap = new HashMap<>();
        error.getAllErrors().forEach((exception) -> {
            String fieldName = exception.getObjectName();
            String errorMessage = exception.getDefaultMessage();
            errorMap.put(fieldName,errorMessage);
        });

        return errorMap;
    }

    @ExceptionHandler(value={DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleException(DataIntegrityViolationException error) {

        HashMap<String,String> errorMap = new HashMap<>();
        errorMap.put("DataIntegrityViolationException", "Product with this code already exists");
        return errorMap;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleException(ProductNotFoundException error) {

        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Product not found", error.getMessage());
        return errorMap;
    }

}
