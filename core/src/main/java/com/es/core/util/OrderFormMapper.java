package com.es.core.util;

import com.es.core.model.order.Order;
import com.es.core.order.OrderForm;

public class OrderFormMapper {
    public static void mapOrderFormFieldsToOrderFields(OrderForm orderForm, Order order) {
        order.setFirstName(orderForm.getFirstName());
        order.setLastName(orderForm.getLastName());
        order.setDeliveryAddress(orderForm.getAddress());
        order.setContactPhoneNo(orderForm.getContactPhoneNo());
        order.setAdditionalInformation(orderForm.getAdditionalInformation());
    }
}
