package org.example.OnlineShop.service;

import org.example.OnlineShop.domain.Phone;
import org.example.OnlineShop.dto.request.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PhoneService {

    Phone getPhoneById(Long phoneId);

    List<Phone> getPopularPhones();

    Page<Phone> getPhonesByFilterParams(SearchRequest searchRequest, Pageable pageable);

    Page<Phone> searchPhones(SearchRequest searchRequest, Pageable pageable);
}
