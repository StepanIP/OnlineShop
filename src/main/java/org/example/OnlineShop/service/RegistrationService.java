package org.example.OnlineShop.service;

import org.example.OnlineShop.dto.request.UserRequest;
import org.example.OnlineShop.dto.response.MessageResponse;

public interface RegistrationService {

    MessageResponse registration(String captchaResponse, UserRequest user);

    MessageResponse activateEmailCode(String code);
}
