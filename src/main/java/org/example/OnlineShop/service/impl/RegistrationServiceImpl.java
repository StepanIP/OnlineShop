package org.example.OnlineShop.service.impl;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.example.OnlineShop.constants.ErrorMessage;
import org.example.OnlineShop.constants.SuccessMessage;
import org.example.OnlineShop.domain.Role;
import org.example.OnlineShop.domain.User;
import org.example.OnlineShop.dto.request.UserRequest;
import org.example.OnlineShop.dto.response.CaptchaResponse;
import org.example.OnlineShop.dto.response.MessageResponse;
import org.example.OnlineShop.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;

    private final String COLLECTION_NAME = "user";
    private final Firestore firestore = FirestoreClient.getFirestore();

    @Value("${recaptcha.url}")
    private String captchaUrl;

    @Value("${recaptcha.secret}")
    private String secret;

    @Override
    @Transactional
    public MessageResponse registration(String captchaResponse, UserRequest userRequest) {
        CollectionReference collection = firestore.collection(COLLECTION_NAME);
        if (userRequest.getPassword() != null && !userRequest.getPassword().equals(userRequest.getPassword2())) {
            return new MessageResponse("passwordError", ErrorMessage.PASSWORDS_DO_NOT_MATCH);
        }
        try {
            if (collection.document(userRequest.getEmail()).get().get().toObject(User.class) != null) {
                return new MessageResponse("emailError", ErrorMessage.EMAIL_IN_USE);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        String url = String.format(captchaUrl, secret, captchaResponse);
        CaptchaResponse response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponse.class);
        if (!response.isSuccess()) {
            return new MessageResponse("captchaError", ErrorMessage.CAPTCHA_ERROR);
        }

        User user = modelMapper.map(userRequest, User.class);
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        collection.document(user.getEmail()).set(user);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("firstName", user.getFirstName());
        attributes.put("activationCode", "/registration/activate/" + user.getActivationCode());
        mailService.sendMessageHtml(user.getEmail(), "Activation code", "registration-template", attributes);
        return new MessageResponse("alert-success", SuccessMessage.ACTIVATION_CODE_SEND);
    }

    @Override
    @Transactional
    public MessageResponse activateEmailCode(String code) {
        CollectionReference collection = firestore.collection(COLLECTION_NAME);
        User user = null;
        try {
            user = collection.document(code).get().get().toObject(User.class);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        if (user == null) {
            return new MessageResponse("alert-danger", ErrorMessage.ACTIVATION_CODE_NOT_FOUND);
        }
        user.setActivationCode(null);
        user.setActive(true);
        collection.document(user.getEmail()).set(user);
        return new MessageResponse("alert-success", SuccessMessage.USER_ACTIVATED);
    }
}
