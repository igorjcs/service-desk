package com.servicedesk.service_desk.controllers;

import com.servicedesk.service_desk.dtos.UserDTO;
import com.servicedesk.service_desk.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> createUser (@RequestBody UserDTO userDTO){
        try {
            userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario cadastrado");
        } catch (ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    public ResponseEntity<String> deleteUser (@PathVariable UUID id){
        try {
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).body("Usuario deletado");
        } catch (ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
