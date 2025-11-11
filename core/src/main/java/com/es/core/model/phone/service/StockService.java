package com.es.core.model.phone.service;

import com.es.core.model.phone.Stock;
import jakarta.annotation.Nullable;

import java.util.List;

public interface StockService {
    List<Stock> findAll(List<Long> phoneIdList);
    Stock get(Long id);
    void updateStock(Stock stock, @Nullable Integer reservedValue, @Nullable Integer stockValue);
}
