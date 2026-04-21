package org.senai.cantina_vidal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.senai.cantina_vidal.controller.api.UserApi;
import org.senai.cantina_vidal.entity.User;
import org.senai.cantina_vidal.service.UserService;
import org.senai.cantina_vidal.dto.user.UserResponseDTO;
import org.senai.cantina_vidal.dto.user.UserPatchRequestDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserApi {
    private final UserService service;

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new UserResponseDTO(service.findById(id)));
    }

    @Override
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new UserResponseDTO(user));
    }

    @Override
    @PatchMapping("/me")
    public ResponseEntity<UserResponseDTO> meUpdate(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid UserPatchRequestDTO dto) {
        return ResponseEntity.ok(new UserResponseDTO(service.update(user.getId(), dto)));
    }

    @Override
    @DeleteMapping("/me")
    public ResponseEntity<Void> meDelete(@AuthenticationPrincipal User user) {
        service.delete(user.getId());
        return ResponseEntity.noContent().build();
    }
}
