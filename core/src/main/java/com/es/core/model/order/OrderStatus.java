package com.es.core.model.order;

public enum OrderStatus {
    NEW, DELIVERED, REJECTED;

    public static OrderStatus fromString(String value) {
        return switch (value.toLowerCase()) {
            case "new" -> NEW;
            case "delivered" -> DELIVERED;
            case "rejected" -> REJECTED;
            default -> null;
        };
    }
}
