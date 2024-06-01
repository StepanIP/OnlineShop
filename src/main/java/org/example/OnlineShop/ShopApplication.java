package org.example.OnlineShop;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
public class ShopApplication {
    public static void main(String[] args) throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("D:\\PetProjects\\OnlineShop1\\src\\main\\resources\\firebase-config.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
        SpringApplication.run(ShopApplication.class, args);
    }

}

