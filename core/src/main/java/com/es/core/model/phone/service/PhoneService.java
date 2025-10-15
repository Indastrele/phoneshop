package com.es.core.model.phone.service;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.util.SortField;
import com.es.core.model.phone.util.SortOrder;

import java.util.List;

public interface PhoneService {
    Long getNumberOfPages(int pageSize, String query);
    Long getNumberOfItems(String query);

    List<Phone> findAll(int offset, int limit, String query, SortOrder order, SortField field);
}
