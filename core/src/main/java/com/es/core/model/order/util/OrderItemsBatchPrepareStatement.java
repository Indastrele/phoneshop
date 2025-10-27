package com.es.core.model.order.util;

import com.es.core.model.order.OrderItem;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderItemsBatchPrepareStatement implements BatchPreparedStatementSetter {
    private List<OrderItem> orderItems;

    public OrderItemsBatchPrepareStatement(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        OrderItem orderItem = orderItems.get(i);
        ps.setObject(1, orderItem.getPhone().getId());
        ps.setObject(2, orderItem.getOrder().getId());
        ps.setObject(3, orderItem.getQuantity());
    }

    @Override
    public int getBatchSize() {
        return orderItems.size();
    }
}
