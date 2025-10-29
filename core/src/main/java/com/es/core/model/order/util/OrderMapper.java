package com.es.core.model.order.util;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class OrderMapper {
    public static Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setOrderItems(new ArrayList<>());
        order.setTotalPrice(rs.getBigDecimal("totalPrice"));
        order.setSubtotal(rs.getBigDecimal("subtotal"));
        order.setDeliveryPrice(rs.getBigDecimal("deliveryPrice"));
        order.setContactPhoneNo(rs.getString("contactPhoneNo"));
        order.setFirstName(rs.getString("firstName"));
        order.setLastName(rs.getString("lastName"));
        order.setDeliveryAddress(rs.getString("deliveryAddress"));
        order.setAdditionalInformation(rs.getString("additionalInformation"));
        order.setPublicId(rs.getObject("publicId", UUID.class));
        order.setStatus(OrderStatus.fromString(rs.getString("status")));
        order.setTimeOfCreation(rs.getTimestamp("timeOfCreation").toLocalDateTime());

        return order;
    }
}
