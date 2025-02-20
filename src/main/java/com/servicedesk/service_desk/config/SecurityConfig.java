package com.servicedesk.service_desk.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
@SecurityScheme(name = SecurityConfig.SECURITY, type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;


    public static final String SECURITY = "bearerAuth";

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception{
        // configs
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                                    // Rotas do swagger
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                                    // Rotas dos endpoints de autenticacao
                                .requestMatchers(HttpMethod.POST, "/auth/register").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                                    // Rotas dos endpoints tickets
                                .requestMatchers(HttpMethod.POST, "/tickets/create").hasAnyRole("ADMIN","USER")
                                .requestMatchers(HttpMethod.DELETE, "/tickets/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/tickets/**").hasAnyRole("USER", "ADMIN")

                                    // Qualquer outra requisicao, precisa de autenticacao
                                .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
