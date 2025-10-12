package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.dao.PhoneDao;
import com.es.core.model.phone.service.PhoneService;
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
    private static final int PAGE_SIZE = 10;
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private PhoneService defaultPhoneService;

    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(@RequestParam(name="query", required = false) String query,
                                  @RequestParam(name="order", required = false) String sortOrder,
                                  @RequestParam(name="field", required = false) String sortField,
                                  @RequestParam(name="page", defaultValue = "1") Integer page,
                                  Model model) {
        Long numOfItems = defaultPhoneService.getNumberOfItems(query);
        Long numberOfPages = defaultPhoneService.getNumberOfPages(PAGE_SIZE, query);
        int offset = (page - 1) * 10;

        model.addAttribute("phones", phoneDao.findAll(offset, PAGE_SIZE, query,
                Optional.ofNullable(sortOrder).map(SortOrder::getFromString).orElse(SortOrder.ASC),
                Optional.ofNullable(sortField).map(SortField::getFromString).orElse(SortField.BRAND)));
        model.addAttribute("currentPage", page);
        model.addAttribute("numberOfPhones", numOfItems);
        model.addAttribute("numberOfPages", numberOfPages);
        
        return "productList";
    }
}
