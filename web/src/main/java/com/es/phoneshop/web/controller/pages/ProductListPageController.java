package com.es.phoneshop.web.controller.pages;

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
    private static final String QUERY = "query";
    private static final String ORDER = "order";
    private static final String FIELD = "field";
    private static final String PAGE = "page";
    private static final String PHONES = "phones";
    private static final String CURRENT_PAGE = "currentPage";
    private static final String NUMBER_OF_PHONES = "numberOfPhones";
    private static final String NUMBER_OF_PAGES = "numberOfPages";
    @Resource
    private PhoneService defaultPhoneService;

    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(@RequestParam(name= QUERY, required = false) String query,
                                  @RequestParam(name= ORDER, required = false) String sortOrder,
                                  @RequestParam(name= FIELD, required = false) String sortField,
                                  @RequestParam(name= PAGE, defaultValue = "1") Integer page,
                                  Model model) {
        Long numOfItems = defaultPhoneService.getNumberOfItems(query);
        Long numberOfPages = defaultPhoneService.getNumberOfPages(PAGE_SIZE, query);
        int offset = (page - 1) * 10;

        model.addAttribute(PHONES, defaultPhoneService.findAll(offset, PAGE_SIZE, query,
                Optional.ofNullable(sortOrder).map(SortOrder::getFromString).orElse(SortOrder.ASC),
                Optional.ofNullable(sortField).map(SortField::getFromString).orElse(SortField.BRAND)));
        model.addAttribute(CURRENT_PAGE, page);
        model.addAttribute(NUMBER_OF_PHONES, numOfItems);
        model.addAttribute(NUMBER_OF_PAGES, numberOfPages);
        
        return "productList";
    }
}
