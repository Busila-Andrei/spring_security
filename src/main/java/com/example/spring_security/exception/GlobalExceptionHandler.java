package com.example.spring_security.exception;

import com.example.spring_security.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(UserAlreadyExistsException.class)
//    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
//        return ResponseEntity.status(HttpStatus.CONFLICT)
//                .body(new ApiResponse<>(e.getMessage(), null));
//    }
//
//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(UserNotFoundException e) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(new ApiResponse<>(e.getMessage(), null));
//    }
//
//    @ExceptionHandler(UnauthorizedException.class)
//    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedException(UnauthorizedException e) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(new ApiResponse<>(e.getMessage(), null));
//    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExistsException(UserAlreadyExistsException e, WebRequest request) {
        return new ResponseEntity<>(new ApiResponse<>(e.getMessage(), null), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(UserNotFoundException e, WebRequest request) {
        return new ResponseEntity<>(new ApiResponse<>(e.getMessage(), null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedException(UnauthorizedException e, WebRequest request) {
        return new ResponseEntity<>(new ApiResponse<>(e.getMessage(), null), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception e, WebRequest request) {
        return new ResponseEntity<>(new ApiResponse<>("An unexpected error occurred.", null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
