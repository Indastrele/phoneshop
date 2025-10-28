package com.es.core.model.phone.service;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.dao.PhoneDao;
import com.es.core.model.phone.util.SortField;
import com.es.core.model.phone.util.SortOrder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultPhoneService implements PhoneService {
    @Resource
    private PhoneDao jdbcPhoneDao;

    @Override
    public Long getNumberOfPages(int pageSize, String query) {
        return (long) Math.ceil(jdbcPhoneDao.getAmountOfPhones(query).doubleValue() / pageSize);
    }

    @Override
    public Long getNumberOfItems(String query) {
        return jdbcPhoneDao.getAmountOfPhones(query);
    }

    @Override
    public List<Phone> findAll(int offset, int limit, String query, SortOrder order, SortField field) {
        return jdbcPhoneDao.findAll(offset, limit, query, order, field);
    }
}
