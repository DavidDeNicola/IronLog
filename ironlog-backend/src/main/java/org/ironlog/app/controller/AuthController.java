package org.ironlog.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.LoginRequestDTO;
import org.ironlog.app.dto.RegisterRequestDTO;
import org.ironlog.app.service.definition.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDTO dto) {
        authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequestDTO dto) {
        String token = authService.login(dto);
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();
    }
}
