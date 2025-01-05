package org.example.productcatalogservice.controllers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// This is global exception handler for all the controllers
@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler({IllegalArgumentException.class, RuntimeException.class})
    public ResponseEntity<String> handleInputException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public void handleInternalException(Exception ex) {
        return;
    }
}
