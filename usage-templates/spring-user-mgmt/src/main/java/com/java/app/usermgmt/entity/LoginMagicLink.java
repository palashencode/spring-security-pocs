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
@Table(name = "login_magic_link")
public class LoginMagicLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "magic_link_id")
    private Integer id;

    @Column(name = "login_token")
    private String token;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username")
    private String username;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "expiry_date")
    private Instant expiryDate;
}
