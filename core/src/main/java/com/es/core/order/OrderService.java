package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order createOrder(Cart cart);
    void placeOrder(Order order) throws OutOfStockException;
    void updateOrder(Order order, OrderStatus orderStatus);
    Order getOrderWithId(Long id);
    Order getOrderWithPublicId(UUID publicId);
    List<Order> getAllOrders();
}
