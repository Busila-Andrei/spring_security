package com.example.spring_security.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String Status;
    private LocalDateTime currentTime;
    private String message;
    private T data;



    public ApiResponse(String message, T data) {
        this.success = true;
        this.currentTime = LocalDateTime.now();
        this.message = message;
        this.data = data;
    }

    public ApiResponse(String message) {
        this.success = true;
        this.currentTime = LocalDateTime.now();
        this.message = message;
    }

    public ApiResponse(String message, boolean success) {
        this.currentTime = LocalDateTime.now();
        this.message = message;
        this.success = success;
    }
}