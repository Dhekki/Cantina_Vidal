package org.senai.cantina_vidal.event;
import org.senai.cantina_vidal.dto.order.OrderResponseDTO;

public record OrderCreatedEvent(OrderResponseDTO orderDTO) {}
