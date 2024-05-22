package org.example.OnlineShop.controller;

import org.example.OnlineShop.constants.Pages;
import org.example.OnlineShop.constants.PathConstants;
import org.example.OnlineShop.dto.request.PhoneRequest;
import org.example.OnlineShop.dto.request.SearchRequest;
import org.example.OnlineShop.dto.response.UserInfoResponse;
import org.example.OnlineShop.service.AdminService;
import org.example.OnlineShop.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(PathConstants.ADMIN)
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ControllerUtils controllerUtils;

    @GetMapping("/phones")
    public String getPhones(Pageable pageable, Model model) {
        controllerUtils.addPagination(model, adminService.getPhones(pageable));
        return Pages.ADMIN_PHONES;
    }

    @GetMapping("/phones/search")
    public String searchPhones(SearchRequest request, Pageable pageable, Model model) {
        controllerUtils.addPagination(request, model, adminService.searchPhones(request, pageable));
        return Pages.ADMIN_PHONES;
    }

    @GetMapping("/users")
    public String getUsers(Pageable pageable, Model model) {
        controllerUtils.addPagination(model, adminService.getUsers(pageable));
        return Pages.ADMIN_ALL_USERS;
    }

    @GetMapping("/users/search")
    public String searchUsers(SearchRequest request, Pageable pageable, Model model) {
        controllerUtils.addPagination(request, model, adminService.searchUsers(request, pageable));
        return Pages.ADMIN_ALL_USERS;
    }

    @GetMapping("/order/{orderId}")
    public String getOrder(@PathVariable Long orderId, Model model) {
        model.addAttribute("order", adminService.getOrder(orderId));
        return Pages.ORDER;
    }

    @GetMapping("/orders")
    public String getOrders(Pageable pageable, Model model) {
        controllerUtils.addPagination(model, adminService.getOrders(pageable));
        return Pages.ORDERS;
    }

    @GetMapping("/orders/search")
    public String searchOrders(SearchRequest request, Pageable pageable, Model model) {
        controllerUtils.addPagination(request, model, adminService.searchOrders(request, pageable));
        return Pages.ORDERS;
    }

    @GetMapping("/phone/{phoneId}")
    public String getPhone(@PathVariable Long phoneId, Model model) {
        model.addAttribute("phone", adminService.getPhoneById(phoneId));
        return Pages.ADMIN_EDIT_PHONE;
    }

    @PostMapping("/edit/phone")
    public String editPhone(@Valid PhoneRequest phone, BindingResult bindingResult, Model model,
                            @RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        if (controllerUtils.validateInputFields(bindingResult, model, "phone", phone)) {
            return Pages.ADMIN_EDIT_PHONE;
        }
        return controllerUtils.setAlertFlashMessage(attributes, "/admin/phones", adminService.editPhone(phone, file));
    }

    @GetMapping("/add/phone")
    public String addPhone() {
        return Pages.ADMIN_ADD_PHONE;
    }

    @PostMapping("/add/phone")
    public String addPhone(@Valid PhoneRequest phone, BindingResult bindingResult, Model model,
                             @RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        if (controllerUtils.validateInputFields(bindingResult, model, "phone", phone)) {
            return Pages.ADMIN_ADD_PHONE;
        }
        return controllerUtils.setAlertFlashMessage(attributes, "/admin/phones", adminService.addPhone(phone, file));
    }

    @GetMapping("/user/{userId}")
    public String getUserById(@PathVariable Long userId, Model model, Pageable pageable) {
        UserInfoResponse userResponse = adminService.getUserById(userId, pageable);
        model.addAttribute("user", userResponse.getUser());
        controllerUtils.addPagination(model, userResponse.getOrders());
        return Pages.ADMIN_USER_DETAIL;
    }
}
