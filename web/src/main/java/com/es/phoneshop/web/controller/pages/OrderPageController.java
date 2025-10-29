package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.model.order.Order;
import com.es.core.order.OrderForm;
import com.es.core.order.OrderService;
import com.es.core.order.OutOfStockException;
import com.es.core.util.OrderFormMapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/order")
public class OrderPageController {
    private static final String ORDER = "order";
    private static final String ORDER_FORM = "orderForm";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String NOT_ENOUGH_STOCK_ERROR_MESSAGE = "There was not enough stock for some items";
    private static final String EMPTY_CART_ERROR_MESSAGE = "Cart has no items";
    @Resource
    private OrderService orderService;
    @Resource
    private CartService httpSessionCartService;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrder(Model model) {
        Order order = orderService.createOrder(httpSessionCartService.getCart());

        model.addAttribute(ORDER,order);
        model.addAttribute(ORDER_FORM, new OrderForm());
        return "order";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String placeOrder(@Valid @ModelAttribute(ORDER_FORM) OrderForm orderForm, BindingResult bindingResult, Model model,
                             RedirectAttributes redirectAttributes)
            throws OutOfStockException {
        if (httpSessionCartService.getCart().getTotalQuantity() == 0) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, EMPTY_CART_ERROR_MESSAGE);
            return "redirect:order";
        }

        Order order = orderService.createOrder(httpSessionCartService.getCart());

        if (bindingResult.hasErrors()) {
            model.addAttribute(ORDER, order);
            model.addAttribute(ORDER_FORM, orderForm);

            return "order";
        }

        OrderFormMapper.mapOrderFormFieldsToOrderFields(orderForm, order);
        orderService.placeOrder(order);

        return "redirect:/orderOverview/".concat(order.getPublicId().toString());
    }

    @ExceptionHandler({OutOfStockException.class})
    public String handleOutOfStock(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ERROR_MESSAGE, NOT_ENOUGH_STOCK_ERROR_MESSAGE);

        return "redirect:order";
    }
}
