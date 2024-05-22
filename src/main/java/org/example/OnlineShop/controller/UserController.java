package org.example.OnlineShop.controller;

import org.example.OnlineShop.constants.Pages;
import org.example.OnlineShop.constants.PathConstants;
import org.example.OnlineShop.dto.request.ChangePasswordRequest;
import org.example.OnlineShop.dto.request.EditUserRequest;
import org.example.OnlineShop.dto.request.SearchRequest;
import org.example.OnlineShop.dto.response.MessageResponse;
import org.example.OnlineShop.service.UserService;
import org.example.OnlineShop.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping(PathConstants.USER)
public class UserController {

    private final UserService userService;
    private final ControllerUtils controllerUtils;

    @GetMapping("/contacts")
    public String contacts() {
        return Pages.CONTACTS;
    }

    @GetMapping("/reset")
    public String passwordReset() {
        return Pages.USER_PASSWORD_RESET;
    }

    @GetMapping("/account")
    public String userAccount(Model model) {
        model.addAttribute("user", userService.getAuthenticatedUser());
        return Pages.USER_ACCOUNT;
    }

    @GetMapping("/orders/search")
    public String searchUserOrders(SearchRequest request, Pageable pageable, Model model) {
        controllerUtils.addPagination(request, model, userService.searchUserOrders(request, pageable));
        return Pages.ORDERS;
    }

    @GetMapping("/info")
    public String userInfo(Model model) {
        model.addAttribute("user", userService.getAuthenticatedUser());
        return Pages.USER_INFO;
    }

    @GetMapping("/info/edit")
    public String editUserInfo(Model model) {
        model.addAttribute("user", userService.getAuthenticatedUser());
        return Pages.USER_INFO_EDIT;
    }

    @PostMapping("/info/edit")
    public String editUserInfo(@Valid EditUserRequest request, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes, Model model) {
        if (controllerUtils.validateInputFields(bindingResult, model, "user", request)) {
            return Pages.USER_INFO_EDIT;
        }
        MessageResponse messageResponse = userService.editUserInfo(request);
        return controllerUtils.setAlertFlashMessage(redirectAttributes, "/user/info", messageResponse);
    }

    @PostMapping("/change/password")
    public String changePassword(@Valid ChangePasswordRequest request, BindingResult bindingResult, Model model) {
        if (controllerUtils.validateInputFields(bindingResult, model, "request", request)) {
            return Pages.USER_PASSWORD_RESET;
        }
        MessageResponse messageResponse = userService.changePassword(request);
        if (controllerUtils.validateInputField(model, messageResponse, "request", request)) {
            return Pages.USER_PASSWORD_RESET;
        }
        return controllerUtils.setAlertMessage(model, Pages.USER_PASSWORD_RESET, messageResponse);
    }
}
