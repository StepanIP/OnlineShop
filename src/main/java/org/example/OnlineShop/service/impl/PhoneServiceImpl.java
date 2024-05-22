package org.example.OnlineShop.service.impl;

import org.example.OnlineShop.constants.ErrorMessage;
import org.example.OnlineShop.domain.Phone;
import org.example.OnlineShop.dto.request.SearchRequest;
import org.example.OnlineShop.repository.PhoneRepository;
import org.example.OnlineShop.service.PhoneService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository phoneRepository;
    private final ModelMapper modelMapper;

    @Override
    public Phone getPhoneById(Long phoneId) {
        return phoneRepository.findById(phoneId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.PHONE_NOT_FOUND));
    }

    @Override
    public List<Phone> getPopularPhones() {
        List<Long> phoneIds = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L);
        return phoneRepository.findByIdIn(phoneIds);
    }

    @Override
    public Page<Phone> getPhonesByFilterParams(SearchRequest request, Pageable pageable) {
        Integer startingPrice = request.getPrice();
        Integer endingPrice = startingPrice + (startingPrice == 0 ? 5000 : 10);
        return phoneRepository.getPhonesByFilterParams(
                request.getBrands(),
                startingPrice,
                endingPrice,
                pageable);
    }

    @Override
    public Page<Phone> searchPhones(SearchRequest request, Pageable pageable) {
        return phoneRepository.searchPhones(request.getSearchType(), request.getText(), pageable);
    }
}
