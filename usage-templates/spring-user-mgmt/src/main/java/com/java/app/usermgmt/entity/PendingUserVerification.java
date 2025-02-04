package com.java.app.usermgmt.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pending_user_verification")
public class PendingUserVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "register_user_id")
    private Integer id;

    private String username;
    private String password;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "registration_time")
    private Instant registrationTime;

    @Column(name = "verification_time")
    private Instant verificationTime;

    private boolean verified;

}
