package org.senai.cantina_vidal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.senai.cantina_vidal.dto.UserResponseDTO;
import org.senai.cantina_vidal.dto.user.UserPatchRequestDTO;
import org.senai.cantina_vidal.entity.User;
import org.senai.cantina_vidal.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new UserResponseDTO(service.findById(id)));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<UserResponseDTO> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new UserResponseDTO(user));
    }

    @PostMapping("/me")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<UserResponseDTO> meUpdate(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid UserPatchRequestDTO dto) {
        return ResponseEntity.ok(new UserResponseDTO(service.update(user.getId(), dto)));
    }

    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<Void> meDelete(@AuthenticationPrincipal User user) {
        service.delete(user.getId());
        return ResponseEntity.noContent().build();
    }
}
