package com.java.app.usermgmt.repository;

import com.java.app.usermgmt.entity.PasswordReset;
import com.java.app.usermgmt.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetRepository extends CrudRepository<PasswordReset, Long> {
    Optional<PasswordReset> findByToken(String token);
    Optional<PasswordReset> findByUser(User user);
}
