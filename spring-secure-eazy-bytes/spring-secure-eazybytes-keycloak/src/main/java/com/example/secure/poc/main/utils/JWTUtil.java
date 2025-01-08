package com.example.secure.poc.main.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class JWTUtil {

    private JWTUtil(){}

    public static String encode(String userName, Collection<? extends GrantedAuthority> authorities, String secretKey){
        return JWT.create()
                .withIssuer("Eazy Bank Sample App")
                .withSubject("JWT Token")
                .withClaim("username", userName)
                .withClaim("authorities", authorities.stream()
                        .map(a -> a.getAuthority()).collect(Collectors.joining(",")))
                .withIssuedAt(new Date())
                .withExpiresAt(Instant.now().plus(8, ChronoUnit.HOURS))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public static Optional<Authentication> decodeToAuthenticatedToken(String jwt, String secretKey) throws BadCredentialsException {
        Authentication authentication = null;
        try{
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey))
                    .build().verify(jwt);
            String username = decodedJWT.getClaim("username").toString();
            List<SimpleGrantedAuthority> authorities = extractAuthorities(decodedJWT);
            authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            return Optional.of(authentication);
        }catch (Exception e){
            throw new BadCredentialsException("Could not validate JWT token");
        }
    }

    private static List<SimpleGrantedAuthority> extractAuthorities(DecodedJWT decodedJWT){
        Claim authClaim = decodedJWT.getClaim("authorities");
        if(authClaim.isNull() || authClaim.isMissing()) return List.of();
        String authValue = String.valueOf(authClaim).replace("\"", "");  // remove trailing double quotes
        return Arrays.stream(authValue.split(",")).map(SimpleGrantedAuthority::new).toList();
    }


}
