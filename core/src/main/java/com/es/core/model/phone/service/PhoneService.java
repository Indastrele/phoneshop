package com.es.core.model.phone.service;

public interface PhoneService {
    Long getNumberOfPages(int pageSize, String query);
    Long getNumberOfItems(String query);
}
