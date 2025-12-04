package org.senai.cantina_vidal.controller;

import lombok.RequiredArgsConstructor;
import org.senai.cantina_vidal.dto.UserResponseDTO;
import org.senai.cantina_vidal.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @GetMapping("/{id}")
    ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new UserResponseDTO(service.findById(id)));
    }
}
