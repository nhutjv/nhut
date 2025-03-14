package com.poly.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import jakarta.annotation.PostConstruct;

@Configuration
public class FirebaseConfig {
	
	 @Bean(name = "firebaseApp1")
	    public FirebaseApp firebaseApp1() throws IOException {
	        if (FirebaseApp.getApps().stream().noneMatch(app -> app.getName().equals("firebaseApp1"))) {
	        	
	        	 InputStream serviceAccount = FirebaseInitializer.class.getClassLoader()
		                    .getResourceAsStream("data.json");

		            if (serviceAccount == null) {
		                throw new IOException("File data.json không tồn tại trong thư mục resources");
		            }

		            FirebaseOptions options = new FirebaseOptions.Builder()
		                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
		                    .build();
		            
	            return FirebaseApp.initializeApp(options, "firebaseApp1");
	        }
	        return FirebaseApp.getInstance("firebaseApp1");
	    }

	    @Bean(name = "firebaseMessaging")
	    public FirebaseMessaging firebaseMessaging() throws IOException {
	        FirebaseApp app = firebaseApp1();
	        System.out.println("thong bao day chay");
	        return FirebaseMessaging.getInstance(app);
	    }

}

