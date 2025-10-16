package com.es.core.cart.util;

import com.es.core.cart.Cart;
import com.es.core.cart.dto.CartDto;
import com.es.core.cart.dto.CartItemDto;

import java.util.HashMap;
import java.util.Map;

public class CartDtoMapper {

    public static CartDto mapCartToCartDto(Cart cart) {
        var cartDto = new CartDto();

        cart.getCartItems().forEach(cartItem -> cartDto.getCartItemDtoList()
                        .add(new CartItemDto(cartItem.getPhone().getId(), cartItem.getQuantity())));

        return cartDto;
    }

    public static Map<Long, Long> mapCartDtoToCartUpdateMap(CartDto cartDto) {
        var cartUpdateMap = new HashMap<Long, Long>();

        cartDto.getCartItemDtoList().forEach(cartItemDto -> cartUpdateMap
                .put(cartItemDto.getPhoneId(), cartItemDto.getQuantity()));

        return cartUpdateMap;
    }
}
