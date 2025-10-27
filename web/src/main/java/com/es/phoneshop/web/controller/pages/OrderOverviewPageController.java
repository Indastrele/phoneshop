package com.es.phoneshop.web.controller.pages;

import com.es.core.model.order.Order;
import com.es.core.order.OrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping(value = "/orderOverview")
public class OrderOverviewPageController {
    @Resource
    private OrderService orderService;
    @GetMapping("/{id}")
    public String mapOrderOverview(@PathVariable(name = "id") UUID id, Model model) {
        Order order = orderService.getOrderWithPublicId(id);

        model.addAttribute("order", order);

        return "orderOverview";
    }
}
