package com.es.core.model.phone.util;

import com.es.core.model.phone.Stock;

@FunctionalInterface
public interface StockUpdateOperation {
    void updateStock(Stock stock);
}
