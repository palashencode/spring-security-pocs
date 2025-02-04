package com.java.app.usermgmt.controller;

import com.java.app.usermgmt.entity.User;
import com.java.app.usermgmt.entity.UserData;
import com.java.app.usermgmt.repository.UserDataRepository;
import com.java.app.usermgmt.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/app")
@RequiredArgsConstructor
public class UserInfoController {

   private final UserRepository userRepository;
   private final UserDataRepository userDataRepository;

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

    /**
     *
     * @param request
     * @return
     */
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request){ // CsrfToken provide by spring security.

        // How generate token
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @GetMapping("/switch-success")
    public Map<String, String> switchSuccess(Authentication authentication){

        return Map.of("app", "spring-user-management",
                "msg", "you have successfully switched user",
                "new-user", authentication.getName(),
                "roles", authentication.getAuthorities().stream()
                        .map(a -> a.getAuthority()).collect(Collectors.toSet()).toString(),
                "currtime", Instant.now().toString());
    }

    @RequestMapping(value = "info", method = { RequestMethod.GET, RequestMethod.POST })
    public Map<String, String> info(Authentication authentication){

        return Map.of("app", "spring-user-management",
                "msg", "app specific info visible only to the user",
                "user", authentication.getName(),
                "roles", authentication.getAuthorities().stream()
                        .map(a -> a.getAuthority()).collect(Collectors.toSet()).toString(),
                "currtime", Instant.now().toString());
    }

    @RequestMapping(value = "data", method = { RequestMethod.GET })
    public List<String> data(Authentication authentication){

        User user = userRepository.findByUsername(authentication.getName()).get();
        List<UserData> userData = userDataRepository.findByUserId(user.getUserId());

        if(userData == null || userData.isEmpty()){
            return new ArrayList<>();
        }

        return userData.stream().map(u -> u.getEntry()).toList();
    }

}
