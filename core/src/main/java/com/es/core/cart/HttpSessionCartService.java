package com.es.core.cart;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.dao.PhoneDao;
import com.es.core.model.phone.dao.StockDao;
import com.es.core.model.phone.exception.InvalidIdException;
import com.es.core.model.phone.exception.DataNotFoundException;
import com.es.core.model.phone.exception.NotEnoughStockException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private StockDao jdbcStockDao;

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
            Stock stock = jdbcStockDao.get(phoneId).orElseThrow(DataNotFoundException::new);
            int availableQuantity = stock.getStock() - stock.getReserved();

            CartItem newItem = new CartItem(phone, quantity);

            cart.getCartItems().stream()
                    .filter(item -> newItem.getPhone().equals(item.getPhone()))
                    .findFirst()
                    .ifPresentOrElse(
                            (item) -> updateOldItem(item, quantity, availableQuantity, phoneId),
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

    private void updateOldItem(CartItem oldItem, Long quantity, int availableQuantity, Long phoneId) {
        long oldQuantity = oldItem.getQuantity();
        updateOldItemWithNewQuantity(oldItem, oldQuantity + quantity, availableQuantity, phoneId);
    }

    @Override
    public void update(Map<Long, Long> items) {
        rwLock.writeLock().lock();
        try {
            cart.getCartItems().forEach(item -> {
                Long phoneId = item.getPhone().getId();
                Long quantity = items.get(item.getPhone().getId());
                if (quantity != null) {
                    Stock stock = jdbcStockDao.get(item.getPhone().getId()).orElseThrow(DataNotFoundException::new);

                    int availableQuantity = stock.getStock() - stock.getReserved();
                    updateOldItemWithNewQuantity(item, quantity, availableQuantity, phoneId);
                }
            });
            setUpdatedTotalCostAndTotalQuantity();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    private void updateOldItemWithNewQuantity(CartItem oldItem, Long quantity, int availableQuantity, Long phoneId) {
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
}
