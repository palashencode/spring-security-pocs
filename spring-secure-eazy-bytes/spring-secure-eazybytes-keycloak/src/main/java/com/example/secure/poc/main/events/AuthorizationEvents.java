package com.example.secure.poc.main.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorizationEvents {

    @EventListener
    public void onFailure(AuthorizationDeniedEvent e){
        log.info("Authorized failed for user : {}, due to {}",
                e.getAuthentication().get().getName(),
                e.getAuthorizationResult().toString());
    }

}
