package com.java.app.usermgmt.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class CustomOAuth2User implements OAuth2User {
    private final OAuth2User oAuth2User;
    private List<String> authorities;
    private String email;
    public CustomOAuth2User(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    public CustomOAuth2User(OAuth2User oAuth2User, List<String> authorities, String email) {
        this.oAuth2User = oAuth2User;
        this.authorities = authorities;
        this.email = email;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Get existing authorities and add new roles
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(oAuth2User.getAuthorities());
        authorities.forEach(a -> updatedAuthorities.add(new SimpleGrantedAuthority(a)));
        return updatedAuthorities;
    }

    @Override
    public String getName() {
//        return oAuth2User.getName(); // Use "login" for GitHub, "email" for Google
        return email;
    }

    public String getEmail() {
        return (String) oAuth2User.getAttributes().get("email");
    }
}
