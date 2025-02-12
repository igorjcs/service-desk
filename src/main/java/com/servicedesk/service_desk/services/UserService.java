package com.servicedesk.service_desk.services;

import com.servicedesk.service_desk.dtos.UserDTO;
import com.servicedesk.service_desk.models.UserModel;
import com.servicedesk.service_desk.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> registerUser (UserModel user){
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Usuario ja existe!");
        }
        // Criptografar senha antes de salvar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole());

        UserModel savedUser = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario cadastrado");
    }

    public void deleteUser(UUID id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Usuario nao encontrado"));
        userRepository.delete(user);
    }



}
