package com.sorion.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sorion.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> NoSuchProduct(NoSuchElementException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
