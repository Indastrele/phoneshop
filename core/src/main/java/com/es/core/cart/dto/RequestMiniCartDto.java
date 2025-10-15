package com.es.core.cart.dto;

import jakarta.validation.constraints.Min;

public class RequestMiniCartDto {
    private Long phoneId;
    @Min(value = 1L, message = "Quantity has to be at least 1")
    private Long quantity;

    public RequestMiniCartDto() {
    }

    public RequestMiniCartDto(Long phoneId, Long quantity) {
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
}
