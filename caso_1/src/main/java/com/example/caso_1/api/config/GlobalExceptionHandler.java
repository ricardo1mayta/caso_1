package com.example.caso_1.api.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnsupportedMediaTypeStatusException.class)
    public ResponseEntity<String> handleUnsupportedMediaType(UnsupportedMediaTypeStatusException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body("El tipo de medio de la solicitud no es compatible.");
    }
}
