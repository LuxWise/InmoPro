package com.example.Inmopro.v1.Exception;

import com.example.Inmopro.v1.Controller.Auth.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AuthResponse> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(AuthResponse.builder().message("Invalid credentials provided").build());
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<AuthResponse> handleLockedException() {
        return ResponseEntity.status(HttpStatus.LOCKED)
                .body(AuthResponse.builder().message("Account is locked. Please contact support.").build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AuthResponse> handleGeneralException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(AuthResponse.builder().message("Internal server error").build());
    }
}
