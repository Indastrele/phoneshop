package com.es.core.cart;

import com.es.core.model.phone.Phone;

import java.util.Objects;

public class CartItem {
    private final Phone phone;
    private Long quantity;

    public CartItem(Phone phone, Long quantity) {
        this.phone = phone;
        this.quantity = quantity;
    }

    public Phone getPhone() {
        return phone;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone, quantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CartItem cartItem = (CartItem) obj;

        return Objects.equals(this.phone, cartItem.phone) && Objects.equals(this.quantity, cartItem.quantity);
    }
}
