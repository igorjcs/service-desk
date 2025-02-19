package com.servicedesk.service_desk.controllers;

import com.servicedesk.service_desk.config.SecurityConfig;
import com.servicedesk.service_desk.dtos.AuthDTO;
import com.servicedesk.service_desk.dtos.LoginResponseDTO;
import com.servicedesk.service_desk.dtos.RegisterDTO;
import com.servicedesk.service_desk.models.UserModel;
import com.servicedesk.service_desk.repositories.UserRepository;
import com.servicedesk.service_desk.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticacao", description = "Autenticacao dos usuarios")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    @Operation(summary = "Fazer login", description = "Método para autenticar usuario")
    public ResponseEntity<?> login(@RequestBody @Validated AuthDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuario", description = "Método para criar usuarios")
    public ResponseEntity<?> register(@RequestBody @Validated RegisterDTO data){
        if (this.userRepository.findByUsername(data.username()) != null)
            return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserModel newUser = new UserModel(data.username(), encryptedPassword, data.role());

        this.userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }

}
