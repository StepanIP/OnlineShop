package org.example.OnlineShop.service;

import org.example.OnlineShop.domain.Order;
import org.example.OnlineShop.domain.Phone;
import org.example.OnlineShop.domain.User;
import org.example.OnlineShop.dto.request.PhoneRequest;
import org.example.OnlineShop.dto.request.SearchRequest;
import org.example.OnlineShop.dto.response.MessageResponse;
import org.example.OnlineShop.dto.response.UserInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {

    Page<Phone> getPhones(Pageable pageable);

    Page<Phone> searchPhones(SearchRequest request, Pageable pageable);

    Page<User> getUsers(Pageable pageable);

    Page<User> searchUsers(SearchRequest request, Pageable pageable);

    Order getOrder(Long orderId);

    Page<Order> getOrders(Pageable pageable);

    Page<Order> searchOrders(SearchRequest request, Pageable pageable);

    Phone getPhoneById(Long phoneId);

    MessageResponse editPhone(PhoneRequest phoneRequest, MultipartFile file);

    MessageResponse addPhone(PhoneRequest phoneRequest, MultipartFile file);

    UserInfoResponse getUserById(Long userId, Pageable pageable);
}
