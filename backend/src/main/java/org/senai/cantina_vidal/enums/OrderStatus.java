package org.senai.cantina_vidal.enums;

public enum OrderStatus {
    RECEIVED,
    IN_PREPARATION,
    DONE,
    DELIVERED,
    NOT_DELIVERED,
    CANCELLED;

    public OrderStatus next() {
        return switch (this) {
            case RECEIVED -> IN_PREPARATION;
            case IN_PREPARATION -> DONE;
            case DONE -> DELIVERED;
            case DELIVERED, NOT_DELIVERED, CANCELLED -> throw new IllegalStateException("O pedido já foi finalizado e seu status não pode ser alterado");
        };
    }

    public boolean isTerminal() {
        return this == DELIVERED || this == NOT_DELIVERED || this == CANCELLED;
    }
}
