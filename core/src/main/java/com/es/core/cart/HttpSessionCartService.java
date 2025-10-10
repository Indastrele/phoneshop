package com.es.core.cart;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HttpSessionCartService implements CartService {
    @Resource
    Cart cart;

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void addPhone(Long phoneId, Long quantity) {
        cart.addToCart(phoneId, quantity);
    }

    @Override
    public void update(Map<Long, Long> items) {
        cart.updateItems(items);
    }

    @Override
    public void remove(Long phoneId) {
        cart.removeItem(phoneId);
    }

    @Override
    public Long getQuantity() {
        return cart.getCartQuantity();
    }
}
