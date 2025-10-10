package com.es.core.cart;

import java.io.Serializable;

public class CartItem implements Serializable {
    private final Long phoneId;
    private Long quantity;

    public CartItem(Long phoneId, Long quantity) {
        this.phoneId = phoneId;
        this.quantity = quantity;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
