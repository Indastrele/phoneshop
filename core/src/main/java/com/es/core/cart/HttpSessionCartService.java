package com.es.core.cart;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.dao.PhoneDao;
import com.es.core.model.phone.exception.InvalidIdException;
import com.es.core.model.phone.exception.NotEnoughStockException;
import com.es.core.model.phone.service.StockService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class HttpSessionCartService implements CartService {
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    @Resource
    private Cart cart;
    @Resource
    private PhoneDao jdbcPhoneDao;
    @Resource
    private StockService stockService;

    @Override
    public Cart getCart() {
        rwLock.readLock().lock();
        try {
            return cart;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public void addPhone(Long phoneId, Long quantity) {
        rwLock.writeLock().lock();
        try {
            Phone phone = jdbcPhoneDao.get(phoneId).orElseThrow(() -> new InvalidIdException(Phone.class, phoneId));
            Stock stock = stockService.get(phoneId);
            int availableQuantity = stock.getStock() - stock.getReserved();

            CartItem newItem = new CartItem(phone, quantity);

            cart.getCartItems().stream()
                    .filter(item -> newItem.getPhone().equals(item.getPhone()))
                    .findFirst()
                    .ifPresentOrElse(
                            (item) -> updateExistingItem(item, quantity, availableQuantity, phoneId),
                            () -> addNewItem(newItem, cart, quantity, availableQuantity, phoneId)
                    );

            setUpdatedTotalCostAndTotalQuantity();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    private void addNewItem(CartItem newItem, Cart cart, Long quantity, int availableQuantity, Long phoneId) {
        checkQuantity(availableQuantity, quantity, phoneId);
        cart.getCartItems().add(newItem);
    }

    private void updateExistingItem(CartItem oldItem, Long quantity, int availableQuantity, Long phoneId) {
        long oldQuantity = oldItem.getQuantity();
        updateExistingItemWithNewQuantity(oldItem, oldQuantity + quantity, availableQuantity, phoneId);
    }

    @Override
    public void update(Map<Long, Long> items) {
        rwLock.writeLock().lock();
        try {
            cart.getCartItems().forEach(item -> {
                Long phoneId = item.getPhone().getId();
                Long quantity = items.get(item.getPhone().getId());
                if (quantity != null) {
                    Stock stock = stockService.get(phoneId);

                    int availableQuantity = stock.getStock() - stock.getReserved();
                    updateExistingItemWithNewQuantity(item, quantity, availableQuantity, phoneId);
                }
            });
            setUpdatedTotalCostAndTotalQuantity();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    private void updateExistingItemWithNewQuantity(CartItem oldItem, Long quantity, int availableQuantity, Long phoneId) {
        checkQuantity(availableQuantity, quantity, phoneId);

        oldItem.setQuantity(quantity);
    }

    private void checkQuantity(int available, long wanted, long phoneId) {
        if (wanted > available) {
            throw new NotEnoughStockException(phoneId, wanted, available);
        }
    }

    @Override
    public void remove(Long phoneId) {
        rwLock.writeLock().lock();
        try {
            cart.getCartItems().removeIf(item -> phoneId.equals(item.getPhone().getId()));

            setUpdatedTotalCostAndTotalQuantity();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public Long getTotalQuantity() {
        return Optional.ofNullable(cart.getTotalQuantity()).orElse(0L);
    }

    @Override
    public BigDecimal getTotalCost() {
        return Optional.ofNullable(cart.getTotalCost()).orElse(BigDecimal.ZERO);
    }

    private void setUpdatedTotalCostAndTotalQuantity() {
        Long totalQuantity = cart.getCartItems().stream()
                .mapToLong(CartItem::getQuantity)
                .sum();
        BigDecimal totalCost = cart.getCartItems().stream()
                .map(item -> item.getPhone().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalQuantity(totalQuantity);
        cart.setTotalCost(totalCost);
    }

    @Override
    public void updateCartItemsStock() {
        List<CartItem> cartItems = cart.getCartItems();

        cartItems.forEach(c -> {
                    Stock s = stockService.get(c.getPhone().getId());
                    stockService.updateStock(s, s.getReserved() + c.getQuantity().intValue(), null);
                });
    }

    private boolean isCartItemStockIsNotSufficient(CartItem cartItem) {
        return Optional.ofNullable(stockService.get(cartItem.getPhone().getId()))
                .filter(s -> s.getStock() - s.getReserved() < cartItem.getQuantity())
                .isPresent();
    }

    @Override
    public void removeAllWithoutStock() {
        rwLock.writeLock().lock();
        try {
            List<CartItem> cartItems = cart.getCartItems();
            List<Long> phoneIdList = cartItems.stream()
                    .filter(this::isCartItemStockIsNotSufficient)
                    .map(item -> item.getPhone().getId())
                    .toList();

            List<Phone> zeroStocksPhoneList = stockService.findAll(phoneIdList).stream()
                    .map(Stock::getPhone)
                    .toList();

            cart.setCartItems(cartItems.stream()
                    .filter(cartItem -> !zeroStocksPhoneList.contains(cartItem.getPhone()))
                    .toList());
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public void removeAll() {
        rwLock.writeLock().lock();
        try {
            cart.getCartItems().clear();
            cart.setTotalCost(BigDecimal.ZERO);
            cart.setTotalQuantity(0L);
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}
