package org.senai.cantina_vidal.enums;

public enum OrderStatus {
    RECEIVED,
    IN_PREPARATION,
    DONE,
    DELIVERED,
    CANCELLED;

    public OrderStatus next() {
        return switch (this) {
            case RECEIVED -> IN_PREPARATION;
            case IN_PREPARATION -> DONE;
            case DONE -> DELIVERED;
            case DELIVERED -> throw new IllegalStateException("O pedido já foi entregue");
            case CANCELLED -> throw new IllegalStateException("O pedido está cancelado");
        };
    }
}
