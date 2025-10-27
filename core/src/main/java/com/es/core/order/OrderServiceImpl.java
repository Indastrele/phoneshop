package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.cart.CartItem;
import com.es.core.cart.CartService;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.order.dao.OrderDao;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.dao.StockDao;
import com.es.core.model.phone.exception.InvalidDaoParamException;
import com.es.core.util.OrderItemMapper;
import jakarta.annotation.Resource;
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
    @Resource
    private CartService cartService;
    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private OrderDao jdbcOrderDao;
    @Resource
    private StockDao jdbcStockDao;

    @Override
    public Order createOrder(Cart cart, BigDecimal deliveryPrice) {
        rwLock.readLock().lock();
        try {
            Order order = new Order();
            List<OrderItem> orderItems = orderItemMapper.mapCartItemsToOrderItems(cart.getCartItems(), order);

            order.setOrderItems(orderItems);
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
    public void placeOrder(Order order, OrderForm orderForm) throws OutOfStockException {
        if (cartService.removeAllWithoutStock() > 0) {
            throw new OutOfStockException();
        }

        List<CartItem> cartItems = cartService.getCart().getCartItems();
        List<Long> phoneIdList = cartItems.stream()
                .map(item -> item.getPhone().getId())
                .toList();
        List<Stock> stockList = jdbcStockDao.findAll(phoneIdList);
        stockList.forEach(stock -> {
            stock.setReserved(stock.getReserved() + cartItems.stream()
                    .filter(item -> stock.getPhone().equals(item.getPhone()))
                    .findFirst()
                    .orElseThrow(InvalidDaoParamException::new)
                    .getQuantity()
                    .intValue());
            jdbcStockDao.save(stock);
        });

        setCustomerInformationAndStatus(order, orderForm);
        jdbcOrderDao.save(order);

        cartService.removeAll();
    }

    private void setCustomerInformationAndStatus(Order order, OrderForm orderForm) {
        order.setStatus(OrderStatus.NEW);

        order.setFirstName(orderForm.getFirstName());
        order.setLastName(orderForm.getLastName());
        order.setContactPhoneNo(orderForm.getContactPhoneNo());
        order.setDeliveryAddress(orderForm.getAddress());
        order.setAdditionalInformation(orderForm.getAdditionalInformation());
    }

    @Override
    public Order getOrderWithPublicId(UUID publicId) {
        return jdbcOrderDao.getWithPublicId(publicId).orElseThrow(InvalidDaoParamException::new);
    }
}
