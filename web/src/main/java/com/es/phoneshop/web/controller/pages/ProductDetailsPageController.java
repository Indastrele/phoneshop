package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.dao.PhoneDao;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping(value = "/productDetails")
public class ProductDetailsPageController {
    private static final String ERROR_MESSAGE = "No such phone with id %d";
    @Resource
    private PhoneDao jdbcPhoneDao;

    @GetMapping(value = "/{id}")
    public String getProductDetailsPage(@PathVariable Long id, Model model) {
        Optional<Phone> phone = jdbcPhoneDao.get(id);

        if (phone.isEmpty()) {
            model.addAttribute("message", String.format(ERROR_MESSAGE, id));
            return "errorPage";
        }

        model.addAttribute("phone", phone.get());

        return "productDetails";
    }
}
