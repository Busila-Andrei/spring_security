package com.example.spring_security.service;


import com.example.spring_security.dto.UserDTO;
import com.example.spring_security.exception.UnauthorizedException;
import com.example.spring_security.exception.UserAlreadyExistsException;
import com.example.spring_security.exception.UserNotFoundException;
import com.example.spring_security.model.User;
import com.example.spring_security.repository.UserRepository;
import com.example.spring_security.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {


    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JWTService jwtService;



    @Override
    public User createUser(RegisterRequest registerRequest) {
        return Optional.of(registerRequest)
                .filter(user -> !userRepository.existsByEmail(registerRequest.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setUsername(registerRequest.getFirstName() + " " + registerRequest.getLastName());
                    user.setEmail(registerRequest.getEmail());
                    user.setPassword(registerRequest.getPassword());
                    return userRepository.save(user);
                }) .orElseThrow(() -> new UserAlreadyExistsException("Oops! " + registerRequest.getEmail() +" already exists!"));
    }

    @Override
    public User getUserByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new UnauthorizedException("You need to be logged in to access this resource.");
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User deleteUserById(int id) {
        return null;
    }

    @Override
    public User deleteUserByEmail(String email) {
        return null;
    }

    @Override
    public UserDTO convertUserToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

}
