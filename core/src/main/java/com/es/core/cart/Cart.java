package com.es.core.cart;

import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart {
    private final List<CartItem> cartItems = new ArrayList<>();
    private Long totalQuantity;
    private BigDecimal totalCost;

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
