package com.es.core.model.phone.dao;

import com.es.core.model.phone.Stock;

import java.util.List;
import java.util.Optional;

public interface StockDao {
    Optional<Stock> get(Long phoneId);
    List<Stock> findAll(List<Long> phoneIdList);
    void save(Stock stock);
}
