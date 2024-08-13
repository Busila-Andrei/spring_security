package com.example.spring_security.request;


import lombok.Data;

@Data
public class RegisterRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;
}
