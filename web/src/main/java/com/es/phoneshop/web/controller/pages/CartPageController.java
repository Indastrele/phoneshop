package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.cart.dto.CartForm;
import com.es.core.cart.util.CartFormMapper;
import com.es.core.model.phone.exception.DataNotFoundException;
import com.es.core.model.phone.exception.InvalidIdException;
import com.es.core.model.phone.exception.NotEnoughStockException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    private static final String CART_FORM = "cartForm";
    private static final String CART = "cart";
    private static final String REDIRECT_CART = "redirect:cart";
    private static final String CART_FORM_BINDING_RESULT = "org.springframework.validation.BindingResult.cartForm";
    private static final String DELETE_CART_ITEM = "deleteCartItem";
    private static final String ERROR_MESSAGE = "errorMessage";
    @Resource
    private CartService cartService;

    @GetMapping
    public String getCart(Model model) {
        Cart cart = cartService.getCart();
        CartForm cartForm = (CartForm) model.getAttribute(CART_FORM);

        if (cartForm == null) {
            cartForm = CartFormMapper.mapCartToCartForm(cart);
        }

        setCartAndCartDto(cart, cartForm, model);
        return CART;
    }

    @PutMapping
    public String updateCart(@Valid @ModelAttribute(CART_FORM) CartForm cartForm, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes, HttpSession session) {
        session.setAttribute(CART_FORM, cartForm);

        if (!bindingResult.hasErrors()) {
            cartService.update(CartFormMapper.mapCartDtoToCartUpdateMap(cartForm));
        } else {
            redirectAttributes.addFlashAttribute(CART_FORM_BINDING_RESULT, bindingResult);
            session.removeAttribute(CART_FORM);
            redirectAttributes.addFlashAttribute(CART_FORM, cartForm);
        }

        return REDIRECT_CART;
    }

    @DeleteMapping
    public String deleteMapping(@RequestParam(value = DELETE_CART_ITEM) Long deletePhoneId) {
        cartService.remove(deletePhoneId);

        return REDIRECT_CART;
    }

    @ExceptionHandler({DataNotFoundException.class, InvalidIdException.class, NotEnoughStockException.class})
    public String handleNotEnoughStock(Exception e, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
        HttpSession session = request.getSession();

        CartForm cartForm = (CartForm) session.getAttribute(CART_FORM);
        if (cartForm != null) {
            session.removeAttribute(CART_FORM);
            redirectAttributes.addFlashAttribute(CART_FORM, cartForm);
        }

        return REDIRECT_CART;
    }

    private void setCartAndCartDto(Cart cart, CartForm cartForm, Model model) {
        model.addAttribute(CART, cart);
        model.addAttribute(CART_FORM, cartForm);
    }
}
