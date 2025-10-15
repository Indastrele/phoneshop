package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.cart.dto.CartDto;
import com.es.core.cart.util.CartDtoMapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(Model model) {
        Cart redirectCart = (Cart) model.getAttribute("cart");
        CartDto redirectCartDto = (CartDto) model.getAttribute("cartDto");

        if (redirectCart != null && redirectCartDto != null) {
            setCartAndCartDto(redirectCart, redirectCartDto, model);
            return "cart";
        }

        Cart cart = cartService.getCart();
        setCartAndCartDto(cart, CartDtoMapper.mapCartToCartDto(cart), model);
        return "cart";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateCart(@Valid @ModelAttribute("cartDto") CartDto cartDto,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model,
                             @RequestParam(value = "deleteCartItem", required = false) Long deleteCartItem) {
        Cart cart = cartService.getCart();

        if (bindingResult.hasErrors()) {
            setCartAndCartDto(cart, cartDto, model);

            return "cart";
        }

        if (deleteCartItem != null) {
            cartService.remove(deleteCartItem);

            return "redirect:cart";
        }

        cartService.update(CartDtoMapper.mapCartDtoToCartUpdateMap(cartDto));

        redirectAttributes.addFlashAttribute("cart", cart);
        redirectAttributes.addFlashAttribute("cartDto", cartDto);

        return "redirect:cart";
    }

    private void setCartAndCartDto(Cart cart, CartDto cartDto, Model model) {
        model.addAttribute("cart", cart);
        model.addAttribute("cartDto", cartDto);
    }
}
