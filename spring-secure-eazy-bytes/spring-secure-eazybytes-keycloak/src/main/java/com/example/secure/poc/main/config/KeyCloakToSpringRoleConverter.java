package com.example.secure.poc.main.config;

import com.example.secure.poc.main.utils.AppConstants;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class KeyCloakToSpringRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> realmAccess = (Map<String, Object>)source.getClaims().get("realm_access");
        if(realmAccess == null || realmAccess.isEmpty()){
            return new ArrayList<>();
        }

        Collection<GrantedAuthority> returnValue = ((List<String>)realmAccess.get("roles"))
                .stream().map(r -> AppConstants.ROLE_PREFIX+r)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return returnValue;
    }
}
