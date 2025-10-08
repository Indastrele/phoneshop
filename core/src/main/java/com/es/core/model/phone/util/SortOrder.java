package com.es.core.model.phone.util;

public enum SortOrder {
    ASC, DESC;

    public static SortOrder getFromString(String value) {
        return switch (value.toLowerCase()) {
            case "asc" -> SortOrder.ASC;
            case "desc" -> SortOrder.DESC;
            default -> null;
        };
    }

    public String getStringValue() {
        return switch (this) {
            case ASC -> "ASC";
            case DESC -> "DESC";
        };
    }
}
