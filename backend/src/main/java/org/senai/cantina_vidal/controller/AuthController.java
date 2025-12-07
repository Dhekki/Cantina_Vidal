package org.senai.cantina_vidal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senai.cantina_vidal.dto.UserResponseDTO;
import org.senai.cantina_vidal.dto.auth.LoginRequestDTO;
import org.senai.cantina_vidal.dto.auth.LoginResponseDTO;
import org.senai.cantina_vidal.dto.auth.RefreshTokenRequestDTO;
import org.senai.cantina_vidal.dto.auth.RegisterRequestDTO;
import org.senai.cantina_vidal.entity.User;
import org.senai.cantina_vidal.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        return ResponseEntity.ok(service.login(data));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid RegisterRequestDTO data) {
        UserResponseDTO user = new UserResponseDTO(service.register(data));
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("{/users/id}").buildAndExpand(user.id()).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(@RequestBody @Valid RefreshTokenRequestDTO data) {
        return ResponseEntity.ok(service.refreshToken(data.refreshToken()));
    }
}
