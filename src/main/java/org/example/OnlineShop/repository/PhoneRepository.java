package org.example.OnlineShop.repository;

import org.example.OnlineShop.domain.Phone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

    List<Phone> findByIdIn(List<Long> phonesIds);

    Page<Phone> findAllByOrderByPriceAsc(Pageable pageable);

    @Query("SELECT phone FROM Phone phone WHERE " +
            "(CASE " +
            "   WHEN :searchType = 'phoneTitle' THEN UPPER(phone.phoneTitle) " +
            "   WHEN :searchType = 'country' THEN UPPER(phone.country) " +
            "   ELSE UPPER(phone.brand) " +
            "END) " +
            "LIKE UPPER(CONCAT('%',:text,'%')) " +
            "ORDER BY phone.price ASC")
    Page<Phone> searchPhones(String searchType, String text, Pageable pageable);

    @Query("SELECT phone FROM Phone phone " +
            "WHERE (coalesce(:brands, null) IS NULL OR phone.brand IN :brands) " +
            "AND (coalesce(:priceStart, null) IS NULL OR phone.price BETWEEN :priceStart AND :priceEnd) " +
            "ORDER BY phone.price ASC")
    Page<Phone> getPhonesByFilterParams(
            List<String> brands,
            Integer priceStart,
            Integer priceEnd,
            Pageable pageable);
}
