package com.es.core.model.order.dao;

import com.es.core.model.order.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderDao {
    List<Order> findAll();
    Optional<Order> get(Long id);
    Optional<Order> getWithPublicId(UUID publicId);
    void save(Order order);
}
