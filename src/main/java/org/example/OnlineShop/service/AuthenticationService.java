package org.example.OnlineShop.service;

import org.example.OnlineShop.dto.request.PasswordResetRequest;
import org.example.OnlineShop.dto.response.MessageResponse;

public interface AuthenticationService {

    MessageResponse sendPasswordResetCode(String email);

    String getEmailByPasswordResetCode(String code);

    MessageResponse resetPassword(PasswordResetRequest request);
}
