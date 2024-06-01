package org.example.OnlineShop.service.impl;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.example.OnlineShop.constants.ErrorMessage;
import org.example.OnlineShop.constants.SuccessMessage;
import org.example.OnlineShop.domain.Order;
import org.example.OnlineShop.domain.User;
import org.example.OnlineShop.dto.request.ChangePasswordRequest;
import org.example.OnlineShop.dto.request.EditUserRequest;
import org.example.OnlineShop.dto.request.SearchRequest;
import org.example.OnlineShop.dto.response.MessageResponse;
import org.example.OnlineShop.repository.OrderRepository;
import org.example.OnlineShop.security.UserPrincipal;
import org.example.OnlineShop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    private final String COLLECTION_NAME = "user";
    private final Firestore firestore = FirestoreClient.getFirestore();;

    @Override
    public User getAuthenticatedUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CollectionReference collection = firestore.collection(COLLECTION_NAME);
        User user;
        try {
            user = collection.document(principal.getUsername()).get().get().toObject(User.class);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public Page<Order> searchUserOrders(SearchRequest request, Pageable pageable) {
        User user = getAuthenticatedUser();
        return orderRepository.searchUserOrders(user.getId(), request.getSearchType(), request.getText(), pageable);
    }

    @Override
    @Transactional
    public MessageResponse editUserInfo(EditUserRequest request) {
        User user = getAuthenticatedUser();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCity(request.getCity());
        user.setAddress(request.getAddress());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPostIndex(request.getPostIndex());
        return new MessageResponse("alert-success", SuccessMessage.USER_UPDATED);
    }

    @Override
    @Transactional
    public MessageResponse changePassword(ChangePasswordRequest request) {
        if (request.getPassword() != null && !request.getPassword().equals(request.getPassword2())) {
            return new MessageResponse("passwordError", ErrorMessage.PASSWORDS_DO_NOT_MATCH);
        }
        User user = getAuthenticatedUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return new MessageResponse("alert-success", SuccessMessage.PASSWORD_CHANGED);
    }
}
