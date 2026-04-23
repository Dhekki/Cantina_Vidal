package org.senai.cantina_vidal.controller;

import lombok.RequiredArgsConstructor;

import org.senai.cantina_vidal.controller.api.SseApi;
import org.senai.cantina_vidal.service.SseService;

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
public class SseController implements SseApi {
    private final SseService service;

    @Override
    @GetMapping(path = "/subscribe/catalog", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeCatalog() {
        return service.subscribeCatalog();
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping(path = "/subscribe/kitchen", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeKitchen() {
        return service.subscribeKitchen();
    }
}
