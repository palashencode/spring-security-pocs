package com.java.app.usermgmt.config;

import com.java.app.usermgmt.entity.User;
import com.java.app.usermgmt.entity.UserRoles;
import com.java.app.usermgmt.repository.UserRepository;
import com.java.app.usermgmt.repository.UserRolesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;

    public CustomOAuth2UserService(UserRepository userRepository, UserRolesRepository userRolesRepository) {
        this.userRepository = userRepository;
        this.userRolesRepository = userRolesRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // Fetch user details from GitHub
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        attributes.forEach((k,v) -> log.info("{}->{}", k, v));

        // Determine the provider (Google or GitHub)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("OAuth2 provider: {}", registrationId);

        String email = null;
        String firstName = null;
        String lastName = null;

        if ("github".equalsIgnoreCase(registrationId)) {
            email = (String) attributes.get("email");
            if (email == null) {
                email = fetchGithubEmail(userRequest.getAccessToken().getTokenValue());
            }
            String name = (String) attributes.get("name");
            if (name != null) {
                String[] nameParts = name.split(" ");
                firstName = nameParts[0];
                lastName = nameParts.length > 1 ? nameParts[1] : "";
            }
        } else if ("google".equalsIgnoreCase(registrationId)) {
            email = (String) attributes.get("email");
            firstName = (String) attributes.get("given_name");
            lastName = (String) attributes.get("family_name");
        }

        if (email == null) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }


//        String username = (String) attributes.get("login");
//        String name = (String) attributes.get("name");
//        String firstName = name.split(" ")[0];
//        String lastName = name.split(" ")[1];
//        String email = (String) attributes.get("email"); // May be null if the email is private

        // Fetch email manually if it's null
//        if (email == null) {
//            email = fetchGithubEmail(userRequest.getAccessToken().getTokenValue());
//        }

//         Check if the user already exists in the database
        Optional<User> userOptional = userRepository.findByUsername(email);
        if (userOptional.isEmpty()) {
            User user = new User();
            user.setUsername(email);
            String invalidPassword = "{bcrypt}$2a$10$invalidpasswordhashthatwontwork";
            user.setPassword(invalidPassword);
            user.setFirstname(firstName);
            user.setLastname(lastName);
            user.setActive(true);
            user = userRepository.save(user);
            userRolesRepository.save(UserRoles.builder()
                    .userId(user.getUserId())
                    .role("ROLE_USER").build());
        }
        userOptional = userRepository.findByUsername(email);
        return new CustomOAuth2User(oAuth2User, userOptional.get()
                                                    .getRoles().stream()
                                                    .map(u -> u.getRole()).toList(), email);
    }

    private String fetchGithubEmail(String accessToken) {
        String apiUrl = "https://api.github.com/user/emails";

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Create request entity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Call GitHub API
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, List.class);

        // Extract email from response
        List<Map<String, Object>> emailList = response.getBody();
        if (emailList != null) {
            for (Map<String, Object> emailObj : emailList) {
                Boolean primary = (Boolean) emailObj.get("primary");
                Boolean verified = (Boolean) emailObj.get("verified");
                if (Boolean.TRUE.equals(primary) && Boolean.TRUE.equals(verified)) {
                    return (String) emailObj.get("email"); // Return primary verified email
                }
            }
        }
        return null;
    }


}
