package org.example.OnlineShop.service.impl;

import org.example.OnlineShop.constants.ErrorMessage;
import org.example.OnlineShop.constants.SuccessMessage;
import org.example.OnlineShop.domain.Order;
import org.example.OnlineShop.domain.Phone;
import org.example.OnlineShop.domain.User;
import org.example.OnlineShop.dto.request.PhoneRequest;
import org.example.OnlineShop.dto.request.SearchRequest;
import org.example.OnlineShop.dto.response.MessageResponse;
import org.example.OnlineShop.dto.response.UserInfoResponse;
import org.example.OnlineShop.repository.OrderRepository;
import org.example.OnlineShop.repository.PhoneRepository;
import org.example.OnlineShop.repository.UserRepository;
import org.example.OnlineShop.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Value("${upload.path}")
    private String uploadPath;

    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<Phone> getPhones(Pageable pageable) {
        return phoneRepository.findAllByOrderByPriceAsc(pageable);
    }

    @Override
    public Page<Phone> searchPhones(SearchRequest request, Pageable pageable) {
        return phoneRepository.searchPhones(request.getSearchType(), request.getText(), pageable);
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> searchUsers(SearchRequest request, Pageable pageable) {
        return userRepository.searchUsers(request.getSearchType(), request.getText(), pageable);
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.getById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND));
    }

    @Override
    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);

    }

    @Override
    public Page<Order> searchOrders(SearchRequest request, Pageable pageable) {
        return orderRepository.searchOrders(request.getSearchType(), request.getText(), pageable);
    }

    @Override
    public Phone getPhoneById(Long phoneId) {
        return phoneRepository.findById(phoneId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.PHONE_NOT_FOUND));
    }

    @Override
    @SneakyThrows
    @Transactional
    public MessageResponse editPhone(PhoneRequest phoneRequest, MultipartFile file) {
        return savePhone(phoneRequest, file, SuccessMessage.PHONE_EDITED);
    }

    @Override
    @SneakyThrows
    @Transactional
    public MessageResponse addPhone(PhoneRequest phoneRequest, MultipartFile file) {
        return savePhone(phoneRequest, file, SuccessMessage.PHONE_ADDED);
    }

    @Override
    public UserInfoResponse getUserById(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        Page<Order> orders = orderRepository.findOrderByUserId(userId, pageable);
        return new UserInfoResponse(user, orders);
    }

    private MessageResponse savePhone(PhoneRequest phoneRequest, MultipartFile file, String message) throws IOException {
        Phone phone = modelMapper.map(phoneRequest, Phone.class);
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            phone.setFilename(resultFilename);
        }
        phoneRepository.save(phone);
        return new MessageResponse("alert-success", message);
    }
}
