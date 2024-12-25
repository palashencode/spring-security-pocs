package com.example.secure.demo.service;

import com.example.secure.demo.entity.UserEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private static final Map<String, UserEntity> dataStore;
    static {
        dataStore = Map.of(
                "test@test.com", getUserEntity(1001L,"test@test.com",
                        "$2a$12$PKGhASdFTPlnga0baArnBekKeR52iHSpw1CD06HfsAG5TkAFM0gyS", "ROLE_USER"),
                "admin@test.com", getUserEntity(1002L,"admin@test.com",
                        "$2a$12$mbplfVmyfGcJDs779NL5peD11ZLQAwJCDV/ey0F2H6YB3FwWXb7Cq", "ROLE_ADMIN"));
    }

    private static UserEntity getUserEntity(long id, String email, String pass, String role){
            return UserEntity.builder()
                    .id(id)
                    .email(email)
                    // testpass
                    .password(pass)
                    .role(role)
                    .build();
    }

    public Optional<UserEntity> findByEmail(String email){
        // DB Call
        if(!dataStore.containsKey(email)) return Optional.empty();
        return Optional.of(dataStore.get(email));
    }

}
