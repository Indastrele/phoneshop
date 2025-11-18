package com.es.core.masscart.form;

import jakarta.validation.constraints.Min;

public class MassCartItemForm {
    private Long code;
    @Min(value = 1, message = "Quantity must be more, than 0")
    private Long quantity;

    public MassCartItemForm() {
    }

    public MassCartItemForm(Long code, Long quantity) {
        this.code = code;
        this.quantity = quantity;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
