package com.java.app.usermgmt.config;

import com.java.app.usermgmt.entity.User;
import com.java.app.usermgmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User details not found for " + username));
        List<? extends GrantedAuthority> roles = user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getRole())).toList();

        Integer userId = user.getUserId();
        String firstName = user.getFirstname();
        String lastName = user.getLastname();

        return new CustomUserDetails(user.getUsername(), user.getPassword(), roles, userId, firstName, lastName);
    }
}
