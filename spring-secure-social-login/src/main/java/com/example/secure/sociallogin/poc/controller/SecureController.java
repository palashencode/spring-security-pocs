package com.example.secure.sociallogin.poc.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Slf4j
@Controller
public class SecureController {

    @GetMapping("/secure")
    public String securePage(Authentication authentication, Model model){
        if(authentication instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken){
            log.info("user from traditional login - {}", usernamePasswordAuthenticationToken.getName());
            model.addAttribute("dataMap", Map.of("id", usernamePasswordAuthenticationToken.getName()));
        }else if(authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken){
            log.info("user from social login - {}", oAuth2AuthenticationToken.getName());
            model.addAttribute("dataMap", printAttr(oAuth2AuthenticationToken));
        }
        log.info("User securely logged in, user = {}", authentication.getName());
        return "secure.html";
    }

    private Map<String, Object> printAttr(OAuth2AuthenticationToken oAuth){
        Map<String, Object> attr = (Map<String, Object>) oAuth.getPrincipal().getAttributes();
        if(null != attr && !attr.isEmpty()){
            attr.forEach((k,v) -> {
                log.info("Key = {}, Value = {}", k, v);
            });
        }
        return attr;
    }

}
