package org.example.OnlineShop.service;

import org.example.OnlineShop.domain.Order;
import org.example.OnlineShop.domain.User;
import org.example.OnlineShop.dto.request.ChangePasswordRequest;
import org.example.OnlineShop.dto.request.EditUserRequest;
import org.example.OnlineShop.dto.request.SearchRequest;
import org.example.OnlineShop.dto.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {

    User getAuthenticatedUser();

    Page<Order> searchUserOrders(SearchRequest request, Pageable pageable);

    MessageResponse editUserInfo(EditUserRequest request);

    MessageResponse changePassword(ChangePasswordRequest request);
}
