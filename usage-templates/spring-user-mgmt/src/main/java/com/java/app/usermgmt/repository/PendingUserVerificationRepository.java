package com.java.app.usermgmt.repository;

import com.java.app.usermgmt.entity.PendingUserVerification;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PendingUserVerificationRepository extends CrudRepository<PendingUserVerification, Long> {

    Optional<PendingUserVerification> findByUsername(String username);
    Optional<PendingUserVerification> findByVerificationToken(String verificationToken);

}
