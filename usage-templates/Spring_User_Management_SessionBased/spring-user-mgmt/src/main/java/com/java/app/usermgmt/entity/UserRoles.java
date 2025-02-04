package com.java.app.usermgmt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_role_mapping")
public class UserRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapping_id")
    @JsonIgnore
    private Integer id;

    @Column(name = "user_id")
    @JsonIgnore
    private Integer userId;
    private String role;
}
