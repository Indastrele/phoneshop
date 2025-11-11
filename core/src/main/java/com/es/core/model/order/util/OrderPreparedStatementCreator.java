package com.es.core.model.order.util;

import com.es.core.model.order.Order;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderPreparedStatementCreator implements PreparedStatementCreator {
    private final Order order;
    private final String sql;

    public OrderPreparedStatementCreator(String sql, Order order) {
        this.sql = sql;
        this.order = order;
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setBigDecimal(1, order.getSubtotal());
        ps.setBigDecimal(2, order.getDeliveryPrice());
        ps.setBigDecimal(3, order.getTotalPrice());
        ps.setString(4, order.getFirstName());
        ps.setString(5, order.getLastName());
        ps.setString(6,  order.getDeliveryAddress());
        ps.setString(7, order.getContactPhoneNo());
        ps.setString(8, order.getAdditionalInformation());
        ps.setString(9, order.getStatus().name());
        ps.setObject(10, order.getPublicId());

        return ps;
    }
}
