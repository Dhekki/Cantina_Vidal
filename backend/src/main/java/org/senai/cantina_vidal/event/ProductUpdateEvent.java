package org.senai.cantina_vidal.event;

public record ProductUpdateEvent(Long productId, Integer newStock, Boolean available) {}
