package org.example.OnlineShop.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.example.OnlineShop.constants.ErrorMessage;
import org.example.OnlineShop.constants.SuccessMessage;
import org.example.OnlineShop.domain.User;
import org.example.OnlineShop.dto.request.PasswordResetRequest;
import org.example.OnlineShop.dto.response.MessageResponse;
import org.example.OnlineShop.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    private final String COLLECTION_NAME = "user";
    private final Firestore firestore = FirestoreClient.getFirestore();
    CollectionReference collection = firestore.collection(COLLECTION_NAME);

    @Override
    @Transactional
    public MessageResponse sendPasswordResetCode(String email) {
        User user = null;
        try {
            user = collection.document(email).get().get().toObject(User.class);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        if (user == null) {
            return new MessageResponse("alert-danger",  ErrorMessage.EMAIL_NOT_FOUND);
        }
        user.setPasswordResetCode(UUID.randomUUID().toString());
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("firstName", user.getFirstName());
        attributes.put("resetCode", "/auth/reset/" + user.getPasswordResetCode());
        mailService.sendMessageHtml(user.getEmail(), "Password reset", "password-reset-template", attributes);
        return new MessageResponse("alert-success",  SuccessMessage.PASSWORD_CODE_SEND);
    }

    public Optional<String> getEmailByPasswordResetCode(String code) {
        CollectionReference users = firestore.collection("user");
        Query query = users.whereEqualTo("passwordResetCode", code);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                User user = document.toObject(User.class);
                return Optional.ofNullable(user.getEmail());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public MessageResponse resetPassword(PasswordResetRequest request) {
        if (request.getPassword() != null && !request.getPassword().equals(request.getPassword2())) {
            return new MessageResponse("passwordError", ErrorMessage.PASSWORDS_DO_NOT_MATCH);
        }
        User user = null;
        try {
            user = collection.document(request.getEmail()).get().get().toObject(User.class);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        if (user == null) {
            return new MessageResponse("alert-danger",  ErrorMessage.EMAIL_NOT_FOUND);
        }        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPasswordResetCode(null);
        return new MessageResponse("alert-success", SuccessMessage.PASSWORD_CHANGED);
    }
}
