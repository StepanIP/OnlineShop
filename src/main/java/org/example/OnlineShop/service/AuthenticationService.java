package org.example.OnlineShop.service;

import org.example.OnlineShop.dto.request.PasswordResetRequest;
import org.example.OnlineShop.dto.response.MessageResponse;

import java.util.Optional;

public interface AuthenticationService {

    MessageResponse sendPasswordResetCode(String email);

    Optional<String> getEmailByPasswordResetCode(String code);

    MessageResponse resetPassword(PasswordResetRequest request);
}
