package com.es.core.cart.dto;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

public class CartDto {
    @Valid
    private List<CartItemDto> cartItemDtoList;

    public CartDto() {
        cartItemDtoList = new ArrayList<>();
    }

    public CartDto(List<CartItemDto> cartItemDtoList) {
        this.cartItemDtoList = cartItemDtoList;
    }

    public List<CartItemDto> getCartItemDtoList() {
        return cartItemDtoList;
    }

    public void setCartItemDtoList(List<CartItemDto> cartItemDtoList) {
        this.cartItemDtoList = cartItemDtoList;
    }
}
