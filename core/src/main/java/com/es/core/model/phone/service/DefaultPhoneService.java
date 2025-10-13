package com.es.core.model.phone.service;

import com.es.core.model.phone.dao.PhoneDao;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
}
