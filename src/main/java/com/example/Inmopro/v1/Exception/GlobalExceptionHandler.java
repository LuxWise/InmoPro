package com.example.Inmopro.v1.Exception;

import com.example.Inmopro.v1.Controller.MonitorCon.MonitorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

// Esta clase maneja todas las excepciones globalmente
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<MonitorResponse> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        MonitorResponse response = MonitorResponse.builder()
                .message(ex.getMessage())
                .data(null)
                .success(false)
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

}