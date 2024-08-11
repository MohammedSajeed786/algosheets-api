package com.algosheets.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handle(BadRequestException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("invalidFields", ex.getInvalidFields());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<String> handle(TokenException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<String> handle(HttpException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatusCode.valueOf(ex.getStatusCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
