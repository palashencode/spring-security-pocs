package com.java.app.usermgmt.repository;

import com.java.app.usermgmt.entity.LoginMagicLink;
import com.java.app.usermgmt.entity.PasswordReset;
import com.java.app.usermgmt.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginMagicLinkRepository extends CrudRepository<LoginMagicLink, Long> {
    Optional<LoginMagicLink> findByToken(String token);
    Optional<LoginMagicLink> findByUserId(Integer userId);
}
