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
    public Stock get(Long phoneId) {
        return jdbcStockDao.get(phoneId).orElseThrow(DataNotFoundException::new);
    }

    @Override
    public void updateNewOrderStock(Stock stock, Integer quantity) {
        stock.setReserved(stock.getReserved() + quantity);
        jdbcStockDao.save(stock);
    }

    @Override
    public void updateConfirmedOrderStock(Stock stock, Integer quantity) {
        stock.setReserved(stock.getReserved() - quantity);
        stock.setStock(stock.getStock() - quantity);
        jdbcStockDao.save(stock);
    }

    @Override
    public void updateRejectedOrderStock(Stock stock, Integer quantity) {
        stock.setReserved(stock.getReserved() - quantity);
        jdbcStockDao.save(stock);
    }

    @Override
    public int getAvailableStock(Stock stock) {
        return stock.getStock() - stock.getReserved();
    }

    @Override
    public boolean hasEnoughStock(Stock stock, int requestedStock) {
        return getAvailableStock(stock) <= requestedStock;
    }
}
