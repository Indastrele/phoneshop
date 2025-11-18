package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.masscart.form.MassCartForm;
import com.es.core.masscart.form.MassCartItemForm;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.exception.DataNotFoundException;
import com.es.core.model.phone.exception.InvalidDataParametersException;
import com.es.core.model.phone.exception.InvalidIdException;
import com.es.core.model.phone.exception.NotEnoughStockException;
import com.es.core.model.phone.service.StockService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/massCart")
public class MassCartController {
    private static final String ERRORS_FLAG = "errorsFlag";
    private static final String MASS_CART_FORM = "massCartForm";
    private static final String PRODUCT_ERRORS = "productErrors";
    private static final String STOCK_ERRORS = "stockErrors";
    private static final String PRODUCT_NOT_FOUND = "Product not found";
    private static final String NOT_ENOUGH_STOCK = "Not enough stock. Only %d is available";
    @Resource
    private CartService cartService;
    @Resource
    private StockService stockService;

    public MassCartController() {
    }

    @GetMapping
    public String getMassCartPage(@RequestParam(required = false) Integer addedCount, Model model) {
        model.addAttribute(MASS_CART_FORM, new MassCartForm());
        return "massCart";
    }

    @PostMapping
    public String addToCart(@Valid @ModelAttribute(MASS_CART_FORM) MassCartForm massCartForm, BindingResult bindingResult, Model model) {
        Map<Long, String> productErrors = new HashMap<>();
        Map<Long, String> stockErrors = new HashMap<>();
        int addedCount = 0;

        if (bindingResult.hasErrors()) {
            model.addAttribute(ERRORS_FLAG, true);
            model.addAttribute(MASS_CART_FORM, massCartForm);
            return "massCart";
        }

        List<MassCartItemForm> massCartItemFormList = massCartForm.getMassCartItemFormList().stream()
                .filter(this::isItemEmpty)
                .collect(Collectors.toList());

        for (int i = 0; i < massCartItemFormList.size(); i++) {
            MassCartItemForm item = massCartItemFormList.get(i);
            Stock itemStock = new Stock();

            try {
                itemStock = stockService.get(item.getCode());
                cartService.addPhone(item.getCode(), item.getQuantity());
            } catch (InvalidIdException | DataNotFoundException | InvalidDataParametersException ex) {
                productErrors.put(item.getCode(), PRODUCT_NOT_FOUND);
                continue;
            } catch (NotEnoughStockException notEnoughStockException) {
                stockErrors.put(item.getCode(),
                        NOT_ENOUGH_STOCK.formatted(stockService.getAvailableStock(itemStock)));
                continue;
            }

            addedCount++;
            massCartItemFormList.set(i, new MassCartItemForm());
        }

        boolean wasAllItemsAdded = massCartItemFormList.stream().anyMatch(item -> !isItemEmpty(item));
        if (wasAllItemsAdded) {
            return "redirect:/massCart?addedCount=%d".formatted(addedCount);
        }

        if (bindingResult.hasErrors() || !productErrors.isEmpty() || !stockErrors.isEmpty()) {
            model.addAttribute(ERRORS_FLAG, true);
            model.addAttribute(MASS_CART_FORM, massCartForm);
            model.addAttribute(PRODUCT_ERRORS, productErrors);
            model.addAttribute(STOCK_ERRORS, stockErrors);
            return "massCart";
        }

        return "redirect:/massCart?addedCount=%d".formatted(addedCount);
    }

    private boolean isItemEmpty(MassCartItemForm item) {
        return item.getCode() != null && item.getQuantity() != null;
    }
}
