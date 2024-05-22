package org.example.OnlineShop.service;

import org.example.OnlineShop.domain.Order;
import org.example.OnlineShop.domain.Phone;
import org.example.OnlineShop.domain.User;
import org.example.OnlineShop.dto.request.OrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    Order getOrder(Long orderId);

    List<Phone> getOrdering();

    Page<Order> getUserOrdersList(Pageable pageable);

    Long postOrder(User user, OrderRequest orderRequest);
}
