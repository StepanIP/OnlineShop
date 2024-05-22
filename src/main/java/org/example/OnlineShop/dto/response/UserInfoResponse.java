package org.example.OnlineShop.dto.response;

import org.example.OnlineShop.domain.Order;
import org.example.OnlineShop.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private User user;
    private Page<Order> orders;
}
