package com.es.core.util;

import com.es.core.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.dao.PhoneDao;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class OrderItemMapper {
    @Resource
    private PhoneDao jdbcPhoneDao;

    public List<OrderItem> mapCartItemsToOrderItems(List<CartItem> cartItems, Order order) {
        return cartItems.stream()
                .map(item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setPhone(item.getPhone());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setOrder(order);

                    return orderItem;
                })
                .toList();
    }

    public OrderItem mapResultSetToOrder(ResultSet rs, Order order) throws SQLException {
        OrderItem orderItem = new OrderItem();

        orderItem.setOrder(order);
        orderItem.setPhone(jdbcPhoneDao.get(rs.getLong("orderItems.phoneId")).get());
        orderItem.setQuantity(rs.getLong("orderItems.quantity"));

        return orderItem;
    }
}
