package com.java.app.usermgmt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
