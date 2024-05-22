package org.example.OnlineShop.service;

import org.example.OnlineShop.domain.Phone;

import java.util.List;

public interface CartService {

    List<Phone> getPhonesInCart();

    void addPhoneToCart(Long phoneId);

    void removePhoneFromCart(Long phoneId);
}
