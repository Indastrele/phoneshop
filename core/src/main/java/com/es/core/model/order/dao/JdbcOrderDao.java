package com.es.core.model.order.dao;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.util.OrderItemsBatchPrepareStatement;
import com.es.core.model.order.util.OrderResultSetExtractor;
import com.es.core.model.phone.exception.InvalidIdException;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcOrderDao implements OrderDao {
    private static final String SELECT_FROM_ORDERS_LEFT_JOIN_ORDER_ITEMS_WHERE_ID =
            "select * from orders left join orderItems oi on orders.id = oi.orderId" +
                    " where orders.id = ?";
    private static final String SELECT_COUNT_DISTINCT_FROM_ORDERS_WHERE_ID = "select COUNT(distinct *) from orders where id = ? ";
    private static final String UPDATE_ORDER = "update orders set subtotal = ?, deliveryPrice = ?, totalPrice = ?, firstName = ?," +
            "lastName = ?, deliveryAddress = ?, contactPhoneNo = ?, additionalInformation = ?, status = ? where id = ?";
    private static final String INSERT_INTO_ORDER_ITEMS = "insert into orderItems ( phoneId, orderId, quantity ) values (?, ?, ?)";
    private static final String INSERT_INTO_ORDER = "insert into orders (subtotal, deliveryPrice, totalPrice, firstName, " +
            "lastName, deliveryAddress, contactPhoneNo, additionalInformation, status, publicId) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
    private static final String SELECT_ALL_FROM_ORDERS = "select * from orders left join orderItems oi on orders.id = oi.orderId";
    private static final String UPDATE_ORDER_ITEMS = "update orderItems set phoneId = ?, orderId = ?, quantity = ? where phoneId = ? and orderId = ?";
    private static final String SELECT_FROM_ORDERS_WHERE_PUBLIC_ID = "select * from orders " +
            "left join orderItems oi on orders.id = oi.orderId " +
            "where orders.publicId = ?";
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private OrderResultSetExtractor orderResultSetExtractor;

    public JdbcOrderDao() {
    }

    @Override
    public List<Order> findAll() {
        return jdbcTemplate.query(SELECT_ALL_FROM_ORDERS, orderResultSetExtractor);
    }

    @Override
    public Optional<Order> get(Long id) {
        if (id == null) {
            throw new InvalidIdException(Order.class, id);
        }

        return jdbcTemplate.query(SELECT_FROM_ORDERS_LEFT_JOIN_ORDER_ITEMS_WHERE_ID, orderResultSetExtractor, id)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Order> getWithPublicId(UUID publicId) {
        if (publicId == null) {
            throw new InvalidIdException(Order.class, null);
        }

        return jdbcTemplate.query(SELECT_FROM_ORDERS_WHERE_PUBLIC_ID, orderResultSetExtractor, publicId)
                .stream()
                .findFirst();
    }

    @Override
    public void save(Order order) {
        if (order.getId() != null && checkIfOrderIsCreated(order.getId())) {
            updateExistingOrder(order);
        } else {
            saveNewOrder(order);
        }
    }

    private boolean checkIfOrderIsCreated(Long id) {
        return jdbcTemplate.queryForObject(SELECT_COUNT_DISTINCT_FROM_ORDERS_WHERE_ID, new SingleColumnRowMapper<Long>(),
                id) > 0;
    }

    private void saveNewOrder(Order order) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(INSERT_INTO_ORDER);
                ps.setBigDecimal(1, order.getSubtotal());
                ps.setBigDecimal(2, order.getDeliveryPrice());
                ps.setBigDecimal(3, order.getTotalPrice());
                ps.setString(4, order.getFirstName());
                ps.setString(5, order.getLastName());
                ps.setString(6,  order.getDeliveryAddress());
                ps.setString(7, order.getContactPhoneNo());
                ps.setString(8, order.getAdditionalInformation());
                ps.setInt(9, order.getStatus().ordinal());

                UUID publicId = UUID.randomUUID();
                ps.setObject(10, publicId);
                order.setPublicId(publicId);

                return ps;
            }
        }, holder);
        order.setId(holder.getKeyAs(Long.class));

        jdbcTemplate.batchUpdate(INSERT_INTO_ORDER_ITEMS, new OrderItemsBatchPrepareStatement(order.getOrderItems()));
    }

    private void updateExistingOrder(Order order) {
        jdbcTemplate.update(UPDATE_ORDER, order.getSubtotal(), order.getDeliveryPrice(), order.getTotalPrice(),
                order.getFirstName(), order.getLastName(), order.getDeliveryAddress(), order.getContactPhoneNo(),
                order.getAdditionalInformation(), order.getStatus().ordinal());

        jdbcTemplate.batchUpdate(UPDATE_ORDER_ITEMS,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        OrderItem orderItem = order.getOrderItems().get(i);
                        ps.setObject(1, orderItem.getPhone().getId());
                        ps.setObject(2, orderItem.getOrder().getId());
                        ps.setObject(3, orderItem.getQuantity());
                        ps.setObject(4, orderItem.getOrder().getId());
                        ps.setObject(5, orderItem.getOrder().getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return order.getOrderItems().size();
                    }
                });

        jdbcTemplate.batchUpdate(INSERT_INTO_ORDER_ITEMS, new OrderItemsBatchPrepareStatement(order.getOrderItems()));
    }
}
