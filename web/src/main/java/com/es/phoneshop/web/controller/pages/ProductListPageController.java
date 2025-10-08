package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.model.phone.dao.PhoneDao;
import com.es.core.model.phone.util.SortField;
import com.es.core.model.phone.util.SortOrder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping (value = "/productList")
public class ProductListPageController {
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(@RequestParam(name="query", required = false) String query,
                                  @RequestParam(name="order", required = false) String sortOrder,
                                  @RequestParam(name="field", required = false) String sortField,
                                  Model model) {
        model.addAttribute("phones", phoneDao.findAll(0, 10, query,
                Optional.ofNullable(sortOrder).map(SortOrder::getFromString).orElse(null),
                Optional.ofNullable(sortField).map(SortField::getFromString).orElse(null)));
        model.addAttribute("cartQuantity", cartService.getQuantity());
        return "productList";
    }
}
