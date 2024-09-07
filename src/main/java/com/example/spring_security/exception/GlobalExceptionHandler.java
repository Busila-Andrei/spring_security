package com.example.spring_security.exception;

import com.example.spring_security.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(e.getMessage(), null));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(e.getMessage(), null));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(e.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ApiResponse<>("Validation failed", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>("An unexpected error occurred.", null));
    }
}
