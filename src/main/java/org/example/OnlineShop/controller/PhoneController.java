package org.example.OnlineShop.controller;

import org.example.OnlineShop.constants.Pages;
import org.example.OnlineShop.constants.PathConstants;
import org.example.OnlineShop.dto.request.SearchRequest;
import org.example.OnlineShop.service.PhoneService;
import org.example.OnlineShop.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(PathConstants.PHONE)
public class PhoneController {

    private final PhoneService phoneService;
    private final ControllerUtils controllerUtils;

    @GetMapping("/{phoneId}")
    public String getPhoneById(@PathVariable Long phoneId, Model model) {
        model.addAttribute("phone", phoneService.getPhoneById(phoneId));
        return Pages.PHONE;
    }

    @GetMapping
    public String getPhonesByFilterParams(SearchRequest request, Model model, Pageable pageable) {
        controllerUtils.addPagination(request, model, phoneService.getPhonesByFilterParams(request, pageable));
        return Pages.PHONES;
    }

    @GetMapping("/search")
    public String searchPhones(SearchRequest request, Model model, Pageable pageable) {
        controllerUtils.addPagination(request, model, phoneService.searchPhones(request, pageable));
        return Pages.PHONES;
    }
}
