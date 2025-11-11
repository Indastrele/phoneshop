package com.es.core.model.order;

import com.es.core.model.phone.Phone;

import java.util.Objects;

public class OrderItem {
    private Long id;
    private Phone phone;
    private Order order;
    private Long quantity;

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(final Phone phone) {
        this.phone = phone;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(final Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, phone, quantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        OrderItem orderItem = (OrderItem) obj;

        return Objects.equals(id, orderItem.id) && Objects.equals(phone, orderItem.phone)
                && Objects.equals(quantity, orderItem.quantity) && Objects.equals(order, orderItem.order);
    }
}
