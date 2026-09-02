package com.example.shoppingcart.exception;

import com.example.shoppingcart.response.ApiResponse;
import com.example.shoppingcart.response.ErrorResponse;
import com.example.shoppingcart.response.MetaResponse;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleKnownException(
            IllegalArgumentException exception) {

        ErrorResponse error = new ErrorResponse(
                "BUSINESS_ERROR",
                exception.getMessage()
        );

        MetaResponse meta = new MetaResponse(
                LocalDateTime.now(),
                "Request failed"
        );

        ApiResponse<Object> response = new ApiResponse<>(
                false,
                null,
                error,
                meta
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(
            MethodArgumentNotValidException exception) {

        String message = exception
                .getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        ErrorResponse error = new ErrorResponse(
                "VALIDATION_ERROR",
                message
        );

        MetaResponse meta = new MetaResponse(
                LocalDateTime.now(),
                "Validation failed"
        );

        ApiResponse<Object> response = new ApiResponse<>(
                false,
                null,
                error,
                meta
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleUnknownException(
            Exception exception) {

        ErrorResponse error = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "Something went wrong"
        );

        MetaResponse meta = new MetaResponse(
                LocalDateTime.now(),
                "Unexpected error occurred"
        );

        ApiResponse<Object> response = new ApiResponse<>(
                false,
                null,
                error,
                meta
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}