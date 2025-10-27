package com.es.core.model.order.util;

import com.es.core.model.order.Order;
import com.es.core.util.OrderItemMapper;
import jakarta.annotation.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderResultSetExtractor implements ResultSetExtractor<List<Order>> {
    @Resource
    private OrderItemMapper orderItemMapper;

    @Override
    public List<Order> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Order> orderMap = new HashMap<>();
        List<Long> idOriginalSequence = new ArrayList<>();

        while(rs.next()) {
            Long orderId = rs.getObject("id", Long.class);
            if (!idOriginalSequence.contains(orderId)) {
                idOriginalSequence.add(orderId);
            }
            Order order = orderMap.computeIfAbsent(orderId, id -> {
                try {
                    return OrderMapper.mapResultSetToOrder(rs);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            Long orderItemId = rs.getObject("orderItems.id", Long.class);
            if (orderItemId != null) {
                order.getOrderItems().add(orderItemMapper.mapResultSetToOrder(rs, order));
            }
        }

        return idOriginalSequence.stream().map(orderMap::get).toList();
    }
}
