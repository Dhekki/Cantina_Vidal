package org.senai.cantina_vidal.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.senai.cantina_vidal.service.SseService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
@Tag(name = "6. Notificações (SSE)", description = "Streams de eventos em tempo real")
@CrossOrigin(origins = "*")
public class SseController {
    private final SseService service;

    @Operation(summary = "Inscrever-se no Catálogo (Público)", description = "Recebe atualizações de estoque e disponibilidade de produtos.")
    @GetMapping(path = "/subscribe/catalog", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeCatalog() {
        return service.subscribeCatalog();
    }

    @Operation(summary = "Inscrever-se na Cozinha (Restrito)", description = "Recebe novos pedidos. Requer role ADMIN ou EMPLOYEE.")
    @SecurityRequirement(name = "bearer-key")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping(path = "/subscribe/kitchen", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeKitchen() {
        return service.subscribeKitchen();
    }
}
