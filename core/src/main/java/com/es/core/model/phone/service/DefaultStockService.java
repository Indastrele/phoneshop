package com.es.core.model.phone.service;

import com.es.core.cart.CartItem;
import com.es.core.cart.CartService;
import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.dao.StockDao;
import com.es.core.model.phone.exception.DataNotFoundException;
import com.es.core.model.phone.exception.InvalidDaoParamException;
import com.es.core.model.phone.util.StockUpdateOperation;
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
        return jdbcStockDao.findAll(phoneIdList);
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

    @Override
    public void updateStocksForDeliveredOrder(List<OrderItem> orderItemList) {
        updateStockWithOrderItemList(orderItemList, stock -> {
                    OrderItem orderItem = orderItemList.stream()
                            .filter(item -> stock.getPhone().equals(item.getPhone()))
                            .findFirst()
                            .orElseThrow(InvalidDaoParamException::new);

                    stock.setReserved(stock.getReserved() - orderItem.getQuantity().intValue());
                    stock.setStock(stock.getStock() - orderItem.getQuantity().intValue());
                });
    }

    @Override
    public void updateStocksForRejectedOrder(List<OrderItem> orderItemList) {
        updateStockWithOrderItemList(orderItemList, (stock) -> stock.setReserved(stock.getReserved()

                - orderItemList.stream()
                .filter(orderItem -> stock.getPhone().equals(orderItem.getPhone()))
                .findFirst()
                .orElseThrow(InvalidDaoParamException::new)
                .getQuantity()
                .intValue()
        ));
    }

    private void updateStockWithOrderItemList(List<OrderItem> orderItemList, StockUpdateOperation stockUpdateOperation) {
        List<Long> phoneIdList = orderItemList.stream().map(orderItem -> orderItem.getPhone().getId()).toList();
        List<Stock> stockList = jdbcStockDao.findAll(phoneIdList);

        stockList.forEach(stockUpdateOperation::updateStock);

        jdbcStockDao.saveAll(stockList);
    }
}
