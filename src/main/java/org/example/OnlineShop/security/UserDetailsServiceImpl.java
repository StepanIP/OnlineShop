package org.example.OnlineShop.security;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.example.OnlineShop.constants.ErrorMessage;
import org.example.OnlineShop.domain.User;
import org.example.OnlineShop.dto.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service("userDetailsServiceImpl")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final String COLLECTION_NAME = "user";
    private final Firestore firestore = FirestoreClient.getFirestore();
    CollectionReference collection = firestore.collection(COLLECTION_NAME);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = null;
        try {
            user = collection.document(email).get().get().toObject(User.class);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (user.getActivationCode() != null ) {
            throw new LockedException("Email not activated");
        }
        return UserPrincipal.create(user);
    }
}
