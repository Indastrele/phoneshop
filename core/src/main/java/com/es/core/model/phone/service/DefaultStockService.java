package com.es.core.model.phone.service;

import com.es.core.cart.CartItem;
import com.es.core.cart.CartService;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.dao.StockDao;
import com.es.core.model.phone.exception.DataNotFoundException;
import com.es.core.model.phone.exception.InvalidDaoParamException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultStockService implements StockService {
    @Resource
    private CartService cartService;
    @Resource
    private StockDao jdbcStockDao;
    @Override
    public List<Stock> findAll(List<Long> phoneIdList) {
        return null;
    }

    @Override
    public Stock get(Long id) {
        return jdbcStockDao.get(id).orElseThrow(DataNotFoundException::new);
    }

    @Override
    public void updateStocksWithCartItems() {
        List<CartItem> cartItems = cartService.getCart().getCartItems();
        List<Long> phoneIdList = cartItems.stream()
                .map(item -> item.getPhone().getId())
                .toList();
        List<Stock> stockList = jdbcStockDao.findAll(phoneIdList);
        stockList.forEach(stock -> {
            stock.setReserved(stock.getReserved()

                    + cartItems.stream()
                    .filter(item -> stock.getPhone().equals(item.getPhone()))
                    .findFirst()
                    .orElseThrow(InvalidDaoParamException::new)
                    .getQuantity()
                    .intValue());
            jdbcStockDao.save(stock);
        });
    }
}
