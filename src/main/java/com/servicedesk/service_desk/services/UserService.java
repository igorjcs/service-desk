package com.servicedesk.service_desk.services;

import com.servicedesk.service_desk.dtos.UserDTO;
import com.servicedesk.service_desk.models.UserModel;
import com.servicedesk.service_desk.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser (UserDTO userDTO){
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuario ja existe");
        }

        UserModel user = new UserModel();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());

        user.setRole(userDTO.getRole());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

    }

    public void deleteUser (UUID id){
        UserModel user = userRepository.findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado"));
        userRepository.delete(user);
    }

}
