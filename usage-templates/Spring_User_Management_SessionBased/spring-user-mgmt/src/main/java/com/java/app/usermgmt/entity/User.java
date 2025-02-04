package com.java.app.usermgmt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    private String username;
    private String firstname;
    private String lastname;
    @JsonIgnore
    private String password;
    private boolean active;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserMeta detail;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<UserRoles> roles;
}
