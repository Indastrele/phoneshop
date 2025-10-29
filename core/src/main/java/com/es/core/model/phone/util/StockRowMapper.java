package com.es.core.model.phone.util;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.dao.PhoneDao;
import com.es.core.model.phone.exception.InvalidIdException;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StockRowMapper implements RowMapper<Stock> {
    @Resource
    private PhoneDao jdbcPhoneDao;

    @Override
    public Stock mapRow(ResultSet rs, int rowNum) throws SQLException {
        var stock = new Stock();

        Long phoneId = rs.getObject("phoneId", Long.class);
        stock.setPhone(jdbcPhoneDao.get(phoneId).orElseThrow(() -> new InvalidIdException(Phone.class, phoneId)));
        stock.setStock(rs.getObject("stock", Integer.class));
        stock.setReserved(rs.getObject("reserved", Integer.class));

        return stock;
    }
}
