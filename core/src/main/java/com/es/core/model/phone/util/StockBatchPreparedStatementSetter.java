package com.es.core.model.phone.util;

import com.es.core.model.phone.Stock;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class StockBatchPreparedStatementSetter implements BatchPreparedStatementSetter {
    private final List<Stock> stockList;

    public StockBatchPreparedStatementSetter(List<Stock> stockList) {
        this.stockList = stockList;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        Stock stock = stockList.get(i);

        ps.setInt(1, stock.getStock());
        ps.setInt(2, stock.getReserved());
        ps.setLong(3, stock.getPhone().getId());
    }

    @Override
    public int getBatchSize() {
        return stockList.size();
    }
}
