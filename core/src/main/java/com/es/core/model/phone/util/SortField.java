package com.es.core.model.phone.util;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;

public enum SortField {
    BRAND, MODEL, PRICE, DISPLAY_SIZE;

    public static SortField getFromString(String value) {
        return switch (value.toLowerCase()) {
            case "brand" -> SortField.BRAND;
            case "model" -> SortField.MODEL;
            case "price" -> SortField.PRICE;
            case "display_size" -> SortField.DISPLAY_SIZE;
            default -> null;
        };
    }

    public String getStringValue() {
        return switch (this) {
            case BRAND -> "brand";
            case MODEL -> "model";
            case PRICE -> "price";
            case DISPLAY_SIZE -> "displaySizeInches";
        };
    }
}
