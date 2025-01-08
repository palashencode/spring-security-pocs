package com.example.secure.poc.main.config;

import com.example.secure.poc.main.utils.AppConstants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenAuthenticationConverter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class KeyCloakOpaqueRoleConverter implements OpaqueTokenAuthenticationConverter {
    @Override
    public Authentication convert(String introspectedToken, OAuth2AuthenticatedPrincipal authenticatedPrincipal) {
        List<String> roles = (List<String>)authenticatedPrincipal.getAttribute("scope");
        Collection<GrantedAuthority> rolesGrantedAuth = roles
                .stream().map(r -> AppConstants.ROLE_PREFIX+r)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new UsernamePasswordAuthenticationToken(authenticatedPrincipal.getName(), null
        ,rolesGrantedAuth);
    }
}
