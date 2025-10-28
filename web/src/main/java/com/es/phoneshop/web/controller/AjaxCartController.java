package com.es.phoneshop.web.controller;

import com.es.core.cart.CartService;
import com.es.core.cart.dto.CartItemForm;
import com.es.core.cart.dto.CartDto;
import com.es.core.cart.dto.ErrorMessageDto;
import com.es.core.model.phone.exception.DataNotFoundException;
import com.es.core.model.phone.exception.InvalidIdException;
import com.es.core.model.phone.exception.NotEnoughStockException;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    private static final String QUANTITY_ERROR_MESSAGE = "Quantity must be at least 1";
    private static final String QUANTITY = "quantity";
    @Resource
    private CartService cartService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CartDto addPhone(@Validated @RequestBody CartItemForm requestBody,
                            BindingResult bindingResult) {
        var responseCartDto = new CartDto();
        if (bindingResult.hasErrors()) {
            String errorMessage = Optional.ofNullable(bindingResult.getFieldError(QUANTITY).getDefaultMessage())
                    .orElse(QUANTITY_ERROR_MESSAGE);
            responseCartDto.setErrorMessage(errorMessage);
        } else {
            cartService.addPhone(requestBody.getPhoneId(), requestBody.getQuantity());
            responseCartDto.setTotalQuantity(cartService.getTotalQuantity());
            responseCartDto.setTotalCost(cartService.getTotalCost());
        }

        return responseCartDto;
    }
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CartDto getCartInfo() {
        var responseCartDto = new CartDto();

        responseCartDto.setTotalQuantity(cartService.getTotalQuantity());
        responseCartDto.setTotalCost(cartService.getTotalCost());
        
        return responseCartDto;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DataNotFoundException.class, InvalidIdException.class, NotEnoughStockException.class})
    public ErrorMessageDto handleExceptions(Exception e) {
        return new ErrorMessageDto(e.getMessage());
    }
}
