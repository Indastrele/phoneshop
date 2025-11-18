package com.es.core.model.phone.service;

import com.es.core.model.phone.Stock;

import java.util.List;

public interface StockService {
    Stock get(Long id);
    void updateNewOrderStock(Stock stock, Integer quantity);
    void updateConfirmedOrderStock(Stock stock, Integer quantity);
    void updateRejectedOrderStock(Stock stock, Integer quantity);
    int getAvailableStock(Stock stock);
    boolean hasEnoughStock(Stock stock, int requestedStock);
}
