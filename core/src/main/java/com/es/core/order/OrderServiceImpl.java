package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.order.dao.OrderDao;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.exception.InvalidDaoParamException;
import com.es.core.model.phone.service.StockService;
import com.es.core.util.OrderItemMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class OrderServiceImpl implements OrderService {
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    @Value("${delivery.price}")
    private BigDecimal deliveryPrice;
    @Resource
    private CartService cartService;
    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private OrderDao jdbcOrderDao;
    @Resource
    private StockService stockService;

    @Override
    public Order createOrder(Cart cart) {
        rwLock.readLock().lock();
        try {
            Order order = new Order();
            List<OrderItem> orderItems = orderItemMapper.mapCartItemsToOrderItems(cart.getCartItems(), order);

            order.setOrderItems(orderItems);
            order.setStatus(OrderStatus.NEW);
            order.setSubtotal(cart.getTotalCost());
            order.setDeliveryPrice(deliveryPrice);
            order.setTotalPrice(cart.getTotalCost().add(deliveryPrice));

            return order;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    @Transactional
    public void placeOrder(Order order) throws OutOfStockException {
        if (hasItemsWithoutStock(order)) {
            cartService.removeAllWithoutStock();
            throw new OutOfStockException();
        }

        cartService.updateCartItemsStock();
        jdbcOrderDao.save(order);

        cartService.removeAll();
    }

    @Override
    public void updateOrder(Order order, OrderStatus orderStatus) {
        if (order.getStatus() == orderStatus) {
            return;
        }

        switch (orderStatus) {
            case DELIVERED -> confirmOrder(order);
            case REJECTED -> rejectOrder(order);
            default -> throw new InvalidDaoParamException();
        }
    }

    private void rejectOrder(Order order) {
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new InvalidDaoParamException();
        }
        order.setStatus(OrderStatus.REJECTED);

        List<OrderItem> orderItemList = order.getOrderItems();
        orderItemList.forEach(oi -> {
            Stock s = stockService.get(oi.getPhone().getId());
            stockService.updateStock(s, s.getReserved() - oi.getQuantity().intValue(), null);
        });

        jdbcOrderDao.save(order);
    }

    private void confirmOrder(Order order) {
        if (order.getStatus() == OrderStatus.REJECTED) {
            throw new InvalidDaoParamException();
        }
        order.setStatus(OrderStatus.DELIVERED);

        List<OrderItem> orderItemList = order.getOrderItems();
        orderItemList.forEach(oi -> {
            Stock s = stockService.get(oi.getPhone().getId());
            stockService.updateStock(s, s.getReserved() - oi.getQuantity().intValue(),
                    s.getStock() - oi.getQuantity().intValue());
        });

        jdbcOrderDao.save(order);
    }

    @Override
    public Order getOrderWithId(Long id) {
        return jdbcOrderDao.get(id).orElseThrow(InvalidDaoParamException::new);
    }

    @Override
    public Order getOrderWithPublicId(UUID publicId) {
        return jdbcOrderDao.getWithPublicId(publicId).orElseThrow(InvalidDaoParamException::new);
    }

    @Override
    public List<Order> getAllOrders() {
        return jdbcOrderDao.findAll();
    }

    private boolean hasItemsWithoutStock(Order order) {
        return order.getOrderItems().stream()
                .anyMatch(oi -> {
                    Stock stock = stockService.get(oi.getPhone().getId());
                    return stock.getStock() - stock.getReserved() < oi.getQuantity();
                });
    }
}
