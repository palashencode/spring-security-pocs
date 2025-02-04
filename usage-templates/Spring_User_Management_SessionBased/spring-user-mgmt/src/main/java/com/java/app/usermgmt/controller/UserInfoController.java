package com.java.app.usermgmt.controller;

import com.java.app.usermgmt.entity.User;
import com.java.app.usermgmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class UserInfoController {

   private final UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getUsers(Authentication authentication){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(u -> users.add(u));
        return users;
    }

    @GetMapping("/user")
    public User getSpecificUsers(@RequestParam(name = "email", required = true) String username){
        User user = userRepository.findByUsername(username).orElse(null);
        return user;
    }

    @GetMapping("/info")
    public Map<String, String> info(Authentication authentication){
        return Map.of("app", "spring-user-management",
                "msg", "app specific info visible only to the user",
                "user", authentication.getName(),
                "currtime", Instant.now().toString());
    }

}
