package com.es.phoneshop.web.controller;

import com.es.core.cart.CartService;
import com.es.core.cart.dto.RequestCartDto;
import com.es.core.cart.dto.ResponseCartDto;
import com.es.core.model.phone.dao.PhoneDao;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService cartService;
    @Resource
    private PhoneDao jdbcPhoneDao;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseCartDto> addPhone(@Validated @RequestBody RequestCartDto requestBody) {

        ResponseCartDto answer = new ResponseCartDto();

        cartService.addPhone(requestBody.getPhoneId(), requestBody.getQuantity());
        answer.setQuantity(cartService.getQuantity());

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(answer);
    }
}
