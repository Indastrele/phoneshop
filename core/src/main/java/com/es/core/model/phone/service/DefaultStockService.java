package com.es.core.model.phone.service;

import com.es.core.model.phone.Stock;
import com.es.core.model.phone.dao.StockDao;
import com.es.core.model.phone.exception.DataNotFoundException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultStockService implements StockService {
    @Resource
    private StockDao jdbcStockDao;
    @Override
    public List<Stock> findAll(List<Long> phoneIdList) {
        return jdbcStockDao.findAll(phoneIdList);
    }

    @Override
    public Stock get(Long phoneId) {
        return jdbcStockDao.get(phoneId).orElseThrow(DataNotFoundException::new);
    }

    @Override
    public void updateStock(Stock stock, Integer reservedValue, Integer stockValue) {
        if (stockValue != null) {
            stock.setStock(stockValue);
        }

        if (reservedValue != null) {
            stock.setReserved(reservedValue);
        }

        jdbcStockDao.save(stock);
    }
}
