package com.es.core.cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Cart implements Serializable {
    // TODO: add cost and quantity of phones
    private final List<CartItem> cartItems = new ArrayList<>();

    public void addToCart(Long phoneId, Long quantity) {
        cartItems.add(new CartItem(phoneId, quantity));
    }

    public void updateItems(Map<Long, Long> items) {
        cartItems.forEach(item -> {
            Long quantity = items.get(item.getPhoneId());
            if (quantity != null) {
                item.setQuantity(quantity);
            }
        });
    }

    public void removeItem(Long phoneId) {
        cartItems.removeIf(item -> phoneId.equals(item.getPhoneId()));
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public Long getCartQuantity() {
        return cartItems.stream().mapToLong(CartItem::getQuantity).sum();
    }
}
