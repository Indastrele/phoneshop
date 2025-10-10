package com.es.core.cart.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ResponseCartDto {
    @NotNull
    @Min(value = 1, message = "Quantity must be at least 1")
    private Long quantity;
    @NotNull
    private BigDecimal cost;

    public ResponseCartDto() {
    }

    @JsonGetter
    public Long getQuantity() {
        return quantity;
    }

    @JsonSetter
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @JsonGetter
    public BigDecimal getCost() {
        return cost;
    }

    @JsonSetter
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
