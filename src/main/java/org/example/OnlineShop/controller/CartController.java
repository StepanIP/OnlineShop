package org.example.OnlineShop.controller;

import org.example.OnlineShop.constants.Pages;
import org.example.OnlineShop.constants.PathConstants;
import org.example.OnlineShop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@RequestMapping(PathConstants.CART)
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String getCart(Model model) {
        model.addAttribute("phones", cartService.getPhonesInCart());
        return Pages.CART;
    }

    @PostMapping("/add")
    public String addPhoneToCart(@RequestParam("phoneId") Long phoneId) {
        cartService.addPhoneToCart(phoneId);
        return "redirect:" + PathConstants.CART;
    }

    @PostMapping("/remove")
    public String removePhoneFromCart(@RequestParam("phoneId") Long phoneId) {
        cartService.removePhoneFromCart(phoneId);
        return "redirect:" + PathConstants.CART;
    }
}
