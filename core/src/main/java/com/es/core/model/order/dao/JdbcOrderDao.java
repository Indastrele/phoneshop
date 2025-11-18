package com.es.core.model.order.dao;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.util.OrderItemsInsertBatchPreparedStatement;
import com.es.core.model.order.util.OrderItemsUpdateBatchPreparedStatement;
import com.es.core.model.order.util.OrderPreparedStatementCreator;
import com.es.core.model.order.util.OrderResultSetExtractor;
import com.es.core.model.phone.exception.InvalidIdException;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcOrderDao implements OrderDao {
    private static final String SELECT_FROM_ORDERS_LEFT_JOIN_ORDER_ITEMS_WHERE_ID =
            "select * from orders left join orderItems oi on orders.id = oi.orderId" +
                    " where orders.id = ?";
    private static final String SELECT_COUNT_DISTINCT_FROM_ORDERS_WHERE_ID = "select COUNT(id) from orders where id = ? ";
    private static final String UPDATE_ORDER = "update orders set subtotal = ?, deliveryPrice = ?, totalPrice = ?, firstName = ?," +
            "lastName = ?, deliveryAddress = ?, contactPhoneNo = ?, additionalInformation = ?, status = ? where id = ?";
    private static final String INSERT_INTO_ORDER_ITEMS = "insert into orderItems ( phoneId, orderId, quantity ) values (?, ?, ?)";
    private static final String INSERT_INTO_ORDER = "insert into orders (subtotal, deliveryPrice, totalPrice, firstName, " +
            "lastName, deliveryAddress, contactPhoneNo, additionalInformation, status, publicId) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
    private static final String SELECT_ALL_FROM_ORDERS = "select * from orders left join orderItems oi on orders.id = oi.orderId";
    private static final String UPDATE_ORDER_ITEMS = "update orderItems set phoneId = ?, orderId = ?, quantity = ? " +
            "where phoneId = ? and orderId = ?";
    private static final String SELECT_FROM_ORDERS_WHERE_PUBLIC_ID = "select * from orders " +
            "left join orderItems oi on orders.id = oi.orderId " +
            "where orders.publicId = ?";
    private static final String COUNT_FROM_ORDER_ITEMS_WHERE_PHONE_ID_AND_ORDER_ID = "select COUNT(*) from orderItems " +
            "where phoneId = ? and orderId = ?";
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

        return CollectionUtils.emptyIfNull(jdbcTemplate.query(SELECT_FROM_ORDERS_LEFT_JOIN_ORDER_ITEMS_WHERE_ID,
                        orderResultSetExtractor, id))
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Order> getWithPublicId(UUID publicId) {
        if (publicId == null) {
            throw new InvalidIdException(Order.class, null);
        }

        return CollectionUtils.emptyIfNull(jdbcTemplate.query(SELECT_FROM_ORDERS_WHERE_PUBLIC_ID, orderResultSetExtractor,
                        publicId))
                .stream()
                .findFirst();
    }

    @Override
    public void save(Order order) {
        if (order.getId() != null && isOrderCreated(order.getId())) {
            updateExistingOrder(order);
        } else {
            saveNewOrder(order);
        }
    }

    private boolean isOrderCreated(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_COUNT_DISTINCT_FROM_ORDERS_WHERE_ID,
                new SingleColumnRowMapper<Long>(), id)).orElse(0L) > 0;
    }

    private void saveNewOrder(Order order) {
        order.setPublicId(UUID.randomUUID());

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new OrderPreparedStatementCreator(INSERT_INTO_ORDER, order), holder);
        order.setId(holder.getKeyAs(Long.class));

        jdbcTemplate.batchUpdate(INSERT_INTO_ORDER_ITEMS, new OrderItemsInsertBatchPreparedStatement(order.getOrderItems()));
    }

    private void updateExistingOrder(Order order) {
        jdbcTemplate.update(UPDATE_ORDER, order.getSubtotal(), order.getDeliveryPrice(), order.getTotalPrice(),
                order.getFirstName(), order.getLastName(), order.getDeliveryAddress(), order.getContactPhoneNo(),
                order.getAdditionalInformation(), order.getStatus().name(), order.getId());

        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderItem> existingOrderItems = orderItems.stream()
                .filter(this::isOrderItemExisting)
                .toList();
        List<OrderItem> newOrderItems = orderItems.stream()
                .filter(item -> !existingOrderItems.contains(item))
                .toList();

        jdbcTemplate.batchUpdate(UPDATE_ORDER_ITEMS, new OrderItemsUpdateBatchPreparedStatement(existingOrderItems));
        jdbcTemplate.batchUpdate(INSERT_INTO_ORDER_ITEMS, new OrderItemsInsertBatchPreparedStatement(newOrderItems));
    }

    private boolean isOrderItemExisting(OrderItem orderItem) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(COUNT_FROM_ORDER_ITEMS_WHERE_PHONE_ID_AND_ORDER_ID,
                        new SingleColumnRowMapper<>(Long.class), orderItem.getPhone().getId(),
                        orderItem.getOrder().getId()))
                .orElse(0L) > 0;
    }
}
