package org.senai.cantina_vidal.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "6. Notificações (SSE)", description = "Streams de eventos em tempo real")
public interface SseApi {

    @Operation(summary = "Inscrever-se no Catálogo (Público)", description = "Recebe atualizações de estoque e disponibilidade de produtos.")
    SseEmitter subscribeCatalog();

    @Operation(summary = "Inscrever-se na Cozinha (Restrito)", description = "Recebe novos pedidos. Requer role ADMIN ou EMPLOYEE.")
    @SecurityRequirement(name = "cookieAuth")
    SseEmitter subscribeKitchen();
}
