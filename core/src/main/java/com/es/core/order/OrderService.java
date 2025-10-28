package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.model.order.Order;

import java.math.BigDecimal;
import java.util.UUID;

public interface OrderService {
    Order createOrder(Cart cart);
    void placeOrder(Order order, OrderForm orderForm) throws OutOfStockException;
    Order getOrderWithPublicId(UUID publicId);
}
