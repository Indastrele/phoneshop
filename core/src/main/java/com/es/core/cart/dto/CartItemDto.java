package com.es.core.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class CartItemDto {
    @NotNull(message = "Phone Id cannot be null")
    private Long phoneId;
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Long quantity;

    public CartItemDto() {
    }

    public CartItemDto(Long phoneId, Long quantity) {
        this.phoneId = phoneId;
        this.quantity = quantity;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneId, quantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CartItemDto cartItemDto = (CartItemDto) obj;

        return Objects.equals(this.phoneId, cartItemDto.phoneId) && Objects.equals(this.quantity, cartItemDto.quantity);
    }
}
