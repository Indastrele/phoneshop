package com.es.core.model.phone.exception;

public class NotEnoughStockException extends RuntimeException {
    public NotEnoughStockException(Long phoneId, Long userQuantity, Integer availableQuantity) {
        super(String.format("Phone: id %d, Available: %d, Wanted: %d", phoneId, availableQuantity, userQuantity));
    }
}
