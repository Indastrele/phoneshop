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

@Controller
@RequestMapping(value = "/admin/orders")
public class OrdersPageController {
    private static final String ORDER = "order";
    private static final String ORDER_LIST = "orderList";
    private static final String ERROR_MESSAGE = "errorMessage";
    @Resource
    private OrderService orderService;
    @GetMapping
    public String orderListPage(Model model) {
        model.addAttribute(ORDER_LIST, orderService.getAllOrders());

        return "orderList";
    }

    @GetMapping("/{id}")
    public String orderPage(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute(ORDER, orderService.getOrderWithId(id));

        return "adminOrderOverview";
    }

    @PatchMapping("/{id}")
    public String updateOrder(@PathVariable(name = "id") Long id, @RequestParam("status") OrderStatus status) {

        Order order = orderService.getOrderWithId(id);
        orderService.updateOrder(order, status);

        return "redirect:/admin/orders/".concat(id.toString());
    }

    @ExceptionHandler({InvalidDaoParamException.class})
    public String handleError(Model model) {
        model.addAttribute(ERROR_MESSAGE, "Something went wrong with data, that you send to the server");

        return "adminErrorPage";
    }
}
