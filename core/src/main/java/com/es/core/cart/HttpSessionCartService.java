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

@Service
public class HttpSessionCartService implements CartService {
    @Resource
    private Cart cart;
    @Resource
    private PhoneDao jdbcPhoneDao;
    @Resource
    private StockDao jdbcStockDao;

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void addPhone(Long phoneId, Long quantity) {
        Phone phone = jdbcPhoneDao.get(phoneId).orElseThrow(() -> new InvalidIdException(Phone.class, phoneId));
        Stock stock = jdbcStockDao.get(phoneId).orElseThrow(DataNotFoundException::new);
        int availableQuantity = stock.getStock() - stock.getReserved();

        CartItem newItem = new CartItem(phone, quantity);

        Optional<CartItem> oldItem = cart.getCartItems().stream()
                .filter(item -> newItem.getPhone().equals(item.getPhone())).findFirst();
        if (oldItem.isPresent()) {
            long oldQuantity = oldItem.get().getQuantity();
            checkQuantity(availableQuantity, oldQuantity + quantity, phoneId);

            oldItem.get().setQuantity(oldQuantity + quantity);
        } else {
            checkQuantity(availableQuantity, quantity, phoneId);

            cart.getCartItems().add(newItem);
        }

        setUpdatedTotalCostAndTotalQuantity();
    }

    private void checkQuantity(int available, long wanted, long phoneId) {
        if (wanted > available) {
            throw new NotEnoughStockException(phoneId, wanted, available);
        }
    }

    @Override
    public void update(Map<Long, Long> items) {
        cart.getCartItems().forEach(item -> {
            Long phoneId = item.getPhone().getId();
            Long quantity = items.get(item.getPhone().getId());
            if (quantity != null) {
                Stock stock = jdbcStockDao.get(item.getPhone().getId()).orElseThrow(DataNotFoundException::new);

                Integer availableQuantity = stock.getStock() - stock.getReserved();
                if (availableQuantity < quantity) {
                    throw new NotEnoughStockException(phoneId, quantity, availableQuantity);
                }

                item.setQuantity(quantity);
            }
        });
        setUpdatedTotalCostAndTotalQuantity();
    }

    @Override
    public void remove(Long phoneId) {
        cart.getCartItems().removeIf(item -> phoneId.equals(item.getPhone().getId()));

        setUpdatedTotalCostAndTotalQuantity();
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
        Long totalQuantity = cart.getCartItems().stream().mapToLong(CartItem::getQuantity).sum();
        BigDecimal totalCost = cart.getCartItems().stream()
                .map(item -> item.getPhone().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalQuantity(totalQuantity);
        cart.setTotalCost(totalCost);
    }
}
