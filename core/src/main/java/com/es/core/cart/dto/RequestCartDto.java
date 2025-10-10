package com.es.core.cart.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.Min;

public class RequestCartDto {
    private Long phoneId;
    @Min(value = 1L, message = "Quantity has to be at least 1")
    private Long quantity;

    public RequestCartDto() {
    }

    public RequestCartDto(Long phoneId, Long quantity) {
        this.phoneId = phoneId;
        this.quantity = quantity;
    }

    @JsonGetter
    public Long getPhoneId() {
        return phoneId;
    }
    @JsonSetter
    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }
    @JsonGetter
    public Long getQuantity() {
        return quantity;
    }
    @JsonSetter
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
