package com.es.core.cart.dto;

public class ErrorMessageDto {
    private String errorMessage;

    public ErrorMessageDto() {
    }

    public ErrorMessageDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
