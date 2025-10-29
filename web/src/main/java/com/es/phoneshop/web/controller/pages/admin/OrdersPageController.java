package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.exception.InvalidDaoParamException;
import com.es.core.order.OrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping(value = "/admin/orders")
public class OrdersPageController {
    private static final String ORDER = "order";
    private static final String ORDER_LIST = "orderList";
    private static final String ERROR_MESSAGE = "errorMessage";
    @Resource
    private OrderService orderService;
    @GetMapping
    public String getOrderListPage(Model model) {
        model.addAttribute(ORDER_LIST, orderService.getAllOrders());

        return "orderList";
    }

    @GetMapping("/{id}")
    public String getOrderPage(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute(ORDER, orderService.getOrderWithId(id));

        return "adminOrderOverview";
    }

    @PatchMapping("/{id}")
    public String patchOrder(@PathVariable(name = "id") Long id, @RequestParam(name = "status") String status) {

        Order order = orderService.getOrderWithId(id);
        orderService.patchOrder(order, Optional.ofNullable(status).map(OrderStatus::fromString).orElse(OrderStatus.NEW));

        return "redirect:/admin/orders/".concat(id.toString());
    }

    @ExceptionHandler({InvalidDaoParamException.class})
    public String handleError(Model model) {
        model.addAttribute(ERROR_MESSAGE, "Something went wrong with data, that you send to the server");

        return "adminErrorPage";
    }
}
