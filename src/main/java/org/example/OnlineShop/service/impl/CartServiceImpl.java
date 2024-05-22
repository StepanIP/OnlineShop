package org.example.OnlineShop.service.impl;

import org.example.OnlineShop.domain.Phone;
import org.example.OnlineShop.domain.User;
import org.example.OnlineShop.repository.PhoneRepository;
import org.example.OnlineShop.service.CartService;
import org.example.OnlineShop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final UserService userService;
    private final PhoneRepository phoneRepository;

    @Override
    public List<Phone> getPhonesInCart() {
        User user = userService.getAuthenticatedUser();
        return user.getPhoneList();
    }

    @Override
    @Transactional
    public void addPhoneToCart(Long phoneId) {
        User user = userService.getAuthenticatedUser();
        Phone phone = phoneRepository.getOne(phoneId);
        user.getPhoneList().add(phone);
    }

    @Override
    @Transactional
    public void removePhoneFromCart(Long phoneId) {
        User user = userService.getAuthenticatedUser();
        Phone phone = phoneRepository.getOne(phoneId);
        user.getPhoneList().remove(phone);
    }
}
