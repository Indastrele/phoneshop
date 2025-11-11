package com.es.core.model.phone.dao;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.exception.InvalidDaoParamException;
import com.es.core.model.phone.exception.InvalidIdException;
import com.es.core.model.phone.util.StockBatchPreparedStatementSetter;
import com.es.core.model.phone.util.StockRowMapper;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcStockDao implements StockDao {
    private static final String SELECT_FROM_STOCKS_WHERE_PHONE_ID = "select * from stocks where phoneId = ?";
    private static final String SELECT_FROM_STOCKS_WHERE_ID_IN = "select phoneId, stock, reserved from stocks " +
            "where phoneId in (%s)";
    private static final String SELECT_COUNT_FROM_STOCKS_WHERE_PHONE_ID = "select COUNT(*) from stocks where phoneId = ?";
    private static final String UPDATE_STOCKS_SET_STOCK_RESERVED_WHERE_PHONE_ID = "update stocks " +
            "set stock = ?, reserved = ? where phoneId = ?";
    private static final String INSERT_INTO_STOCKS_PHONE_ID_STOCK_RESERVED_VALUES = "insert into stocks " +
            "(stock, reserved, phoneId) values(?, ?, ?)";
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private StockRowMapper stockRowMapper;

    @Override
    public Optional<Stock> get(Long phoneId) {
        if (phoneId == null) {
            throw new InvalidIdException(Phone.class, phoneId);
        }

        return CollectionUtils.emptyIfNull(jdbcTemplate.query(SELECT_FROM_STOCKS_WHERE_PHONE_ID, stockRowMapper, phoneId))
                .stream()
                .findFirst();
    }

    @Override
    public List<Stock> findAll(List<Long> phoneIdList) {
        String phonesIdString = String.join(", ", phoneIdList.stream().map(String::valueOf).toList());
        return (List<Stock>) CollectionUtils.emptyIfNull(jdbcTemplate.query(String.format(SELECT_FROM_STOCKS_WHERE_ID_IN,
                        phonesIdString), stockRowMapper));
    }

    @Override
    public void save(Stock stock) {
        if (stock.getPhone() == null) {
            throw new InvalidDaoParamException();
        }

        Long phoneId = stock.getPhone().getId();
        if (checkIfPhoneHaveStockInDatabase(phoneId)) {
            jdbcTemplate.update(UPDATE_STOCKS_SET_STOCK_RESERVED_WHERE_PHONE_ID, stock.getStock(),
                    stock.getReserved(), phoneId);
        } else {
            jdbcTemplate.update(INSERT_INTO_STOCKS_PHONE_ID_STOCK_RESERVED_VALUES, stock.getStock(),
                    stock.getReserved(), phoneId);
        }
    }

    @Override
    public void saveAll(List<Stock> stockList) {
        if (stockList == null || stockList.isEmpty()) {
            throw new InvalidDaoParamException();
        }

        List<Stock> existingStocks = stockList.stream()
                .filter(stock -> checkIfPhoneHaveStockInDatabase(stock.getPhone().getId()))
                .toList();

        List<Stock> newStocks = stockList.stream()
                .filter(stock -> !existingStocks.contains(stock))
                .toList();

        jdbcTemplate.batchUpdate(UPDATE_STOCKS_SET_STOCK_RESERVED_WHERE_PHONE_ID,
                new StockBatchPreparedStatementSetter(existingStocks));

        jdbcTemplate.batchUpdate(INSERT_INTO_STOCKS_PHONE_ID_STOCK_RESERVED_VALUES,
                new StockBatchPreparedStatementSetter(newStocks));
    }

    private boolean checkIfPhoneHaveStockInDatabase(Long phoneId) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_COUNT_FROM_STOCKS_WHERE_PHONE_ID,
                new SingleColumnRowMapper<>(Long.class), phoneId)).orElse(0L) > 0;
    }
}
