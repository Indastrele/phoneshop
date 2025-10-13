package com.es.core.cart;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.dao.PhoneDao;
import com.es.core.model.phone.exception.InvalidIdException;
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

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void addPhone(Long phoneId, Long quantity) {
        cart.getCartItems().add(new CartItem(jdbcPhoneDao.get(phoneId)
                .orElseThrow(() -> new InvalidIdException(Phone.class, phoneId)), quantity));

        setUpdatedTotalCostAndTotalQuantity();
    }

    @Override
    public void update(Map<Long, Long> items) {
        cart.getCartItems().forEach(item -> {
            Long quantity = items.get(item.getPhone().getId());
            if (quantity != null) {
                item.setQuantity(quantity);
            }
        });
        setUpdatedTotalCostAndTotalQuantity();
    }

    @Override
    public void remove(Long phoneId) {
        cart.getCartItems().removeIf(item -> phoneId.equals(item.getPhone().getId()));
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
        BigDecimal totalCost = cart.getCartItems().stream().map(item -> item.getPhone().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalQuantity(totalQuantity);
        cart.setTotalCost(totalCost);
    }
}
