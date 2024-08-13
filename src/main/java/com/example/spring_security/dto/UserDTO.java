package com.example.spring_security.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Data
public class UserDTO {

    private String id;
    private String username;
    private String email;

}
