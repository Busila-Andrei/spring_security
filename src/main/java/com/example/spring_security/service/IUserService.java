package com.example.spring_security.service;

import com.example.spring_security.dto.UserDTO;
import com.example.spring_security.model.User;
import com.example.spring_security.request.LoginRequest;
import com.example.spring_security.request.RegisterRequest;

public interface IUserService {

    User createUser(RegisterRequest registerRequest);
    User loginUser(LoginRequest loginRequest);

    User getUserByEmail(String email);
    User getUserById(long id);



    User deleteUserById(int id);
    User deleteUserByEmail(String email);

    UserDTO convertUserToUserDTO(User user);


}
