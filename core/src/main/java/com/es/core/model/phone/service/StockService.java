package com.es.core.model.phone.service;

import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.Stock;

import java.util.List;

public interface StockService {
    List<Stock> findAll(List<Long> phoneIdList);
    Stock get(Long id);
    void updateStocksWithCartItems();
    void updateStocksForDeliveredOrder(List<OrderItem> orderItemList);
    void updateStocksForRejectedOrder(List<OrderItem> orderItemList);
}
