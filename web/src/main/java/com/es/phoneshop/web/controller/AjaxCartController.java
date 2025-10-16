package com.es.phoneshop.web.controller;

import com.es.core.cart.CartService;
import com.es.core.cart.dto.RequestMiniCartDto;
import com.es.core.cart.dto.ResponseMiniCartDto;
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
    @Resource
    private CartService cartService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMiniCartDto addPhone(@Validated @RequestBody RequestMiniCartDto requestBody,
                                        BindingResult bindingResult) {
        var responseCartDto = new ResponseMiniCartDto();
        if (bindingResult.hasErrors()) {
            String errorMessage = Optional.ofNullable(bindingResult.getFieldError("quantity").getDefaultMessage())
                    .orElse("Quantity must be at least 1");
            responseCartDto.setErrorMessage(errorMessage);
        } else {
            cartService.addPhone(requestBody.getPhoneId(), requestBody.getQuantity());
            responseCartDto.setTotalQuantity(cartService.getTotalQuantity());
            responseCartDto.setTotalCost(cartService.getTotalCost());
        }

        return responseCartDto;
    }
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMiniCartDto getCartInfo() {
        var responseCartDto = new ResponseMiniCartDto();

        responseCartDto.setTotalQuantity(cartService.getTotalQuantity());
        responseCartDto.setTotalCost(cartService.getTotalCost());
        
        return responseCartDto;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DataNotFoundException.class, InvalidIdException.class, NotEnoughStockException.class})
    public ResponseMiniCartDto handleExceptions(Exception e) {
        var responseCartDto = new ResponseMiniCartDto();
        responseCartDto.setErrorMessage(e.getMessage());

        return responseCartDto;
    }
}
