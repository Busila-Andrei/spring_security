package com.example.spring_security.service;


import com.example.spring_security.dto.UserDTO;
import com.example.spring_security.exception.UnauthorizedException;
import com.example.spring_security.exception.UserAlreadyExistsException;
import com.example.spring_security.exception.UserNotFoundException;
import com.example.spring_security.model.User;
import com.example.spring_security.repository.UserRepository;
import com.example.spring_security.request.LoginRequest;
import com.example.spring_security.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EncryptionService encryptionService;

    public UserDTO createUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Oops! " + registerRequest.getEmail() +" already exists!");
        }

        User user = new User();
        user.setUsername(registerRequest.getFirstName() + " " + registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encryptionService.encryptPassword(registerRequest.getPassword()));
        userRepository.save(user);
        return convertUserToUserDTO(user);
    }

    public UserDTO loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + loginRequest.getEmail()));

        if (!encryptionService.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials.");
        }
        return convertUserToUserDTO(user);
    }

//    @Override
//    public User getUserByEmail(String email) {
//        if (email == null || email.isEmpty()) {
//            throw new UnauthorizedException("You need to be logged in to access this resource.");
//        }
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
//
//    }


    public UserDTO convertUserToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

}
