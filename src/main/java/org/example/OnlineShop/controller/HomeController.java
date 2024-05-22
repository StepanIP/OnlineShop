package org.example.OnlineShop.controller;

import org.example.OnlineShop.constants.Pages;
import org.example.OnlineShop.service.PhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PhoneService phoneService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("phones", phoneService.getPopularPhones());
        return Pages.HOME;
    }
}
