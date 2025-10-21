package com.es.core.cart.util;

import com.es.core.cart.Cart;
import com.es.core.cart.dto.CartForm;
import com.es.core.cart.dto.CartItemForm;

import java.util.HashMap;
import java.util.Map;

public class CartFormMapper {

    public static CartForm mapCartToCartForm(Cart cart) {
        var cartDto = new CartForm();

        cart.getCartItems().forEach(cartItem -> cartDto.getCartItemFormList()
                        .add(new CartItemForm(cartItem.getPhone().getId(), cartItem.getQuantity())));

        return cartDto;
    }

    public static Map<Long, Long> mapCartDtoToCartUpdateMap(CartForm cartForm) {
        var cartUpdateMap = new HashMap<Long, Long>();

        cartForm.getCartItemFormList().forEach(cartItemForm -> cartUpdateMap
                .put(cartItemForm.getPhoneId(), cartItemForm.getQuantity()));

        return cartUpdateMap;
    }
}
