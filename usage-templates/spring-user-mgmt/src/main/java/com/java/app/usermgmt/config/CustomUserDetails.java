package com.java.app.usermgmt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Acts both as user principal and subclass of UserDetails
 */
@Getter
@Setter
public class CustomUserDetails extends User {

    private Integer userId;
    private String firstName;
    private String lastName;

    public String getName(){
        return firstName + " " + lastName;  // Example: full name
    }

    public CustomUserDetails(String username,
                             String password,
                             Collection<? extends GrantedAuthority> authorities,
                             Integer userId,
                             String firstName,
                             String lastName) {
        super(username, password, authorities);
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public CustomUserDetails(String username,
                             String password,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public CustomUserDetails(String username, String password,
                             boolean enabled, boolean accountNonExpired,
                             boolean credentialsNonExpired, boolean accountNonLocked,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
