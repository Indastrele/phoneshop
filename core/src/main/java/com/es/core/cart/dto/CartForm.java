package com.es.core.cart.dto;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

public class CartForm {
    @Valid
    private List<CartItemForm> cartItemFormList;

    public CartForm() {
        cartItemFormList = new ArrayList<>();
    }

    public CartForm(List<CartItemForm> cartItemFormList) {
        this.cartItemFormList = cartItemFormList;
    }

    public List<CartItemForm> getCartItemFormList() {
        return cartItemFormList;
    }

    public void setCartItemDtoList(List<CartItemForm> cartItemFormList) {
        this.cartItemFormList = cartItemFormList;
    }
}
