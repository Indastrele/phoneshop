package com.es.phoneshop.web.controller;

import com.es.core.cart.CartService;
import com.es.core.cart.dto.RequestCartDto;
import com.es.core.cart.dto.ResponseCartDto;
import com.es.core.model.phone.exception.InvalidIdException;
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

@RestController
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService cartService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseCartDto addPhone(@Validated @RequestBody RequestCartDto requestBody,
                                                    BindingResult bindingResult) {
        var responseCartDto = new ResponseCartDto();
        if (bindingResult.hasErrors()) {
            responseCartDto.setErrorMessage(bindingResult.getFieldError("quantity").getDefaultMessage());
        } else {
            cartService.addPhone(requestBody.getPhoneId(), requestBody.getQuantity());
            responseCartDto.setTotalQuantity(cartService.getTotalQuantity());
            responseCartDto.setTotalCost(cartService.getTotalCost());
        }

        return responseCartDto;
    }
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseCartDto getCartInfo() {
        var responseCartDto = new ResponseCartDto();

        responseCartDto.setTotalQuantity(cartService.getTotalQuantity());
        responseCartDto.setTotalCost(cartService.getTotalCost());
        
        return responseCartDto;
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidIdException.class)
    public ResponseCartDto handleInvalidIdDataBaseQuery(Exception e) {
        var responseCartDto = new ResponseCartDto();
        responseCartDto.setErrorMessage(e.getMessage());

        return responseCartDto;
    }
}
