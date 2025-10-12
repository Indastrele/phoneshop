package com.es.core.model.phone.dao;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.util.SortField;
import com.es.core.model.phone.util.SortOrder;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(Long key);
    void save(Phone phone);
    List<Phone> findAll(int offset, int limit, String query, SortOrder order, SortField field);
    Long getAmountOfPhones(String query);
}
