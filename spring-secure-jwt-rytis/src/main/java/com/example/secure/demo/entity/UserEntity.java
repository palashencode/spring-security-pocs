package com.example.secure.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEntity {

    private Long id;

    private String email;

    @JsonIgnore
    private String password;

    private String role;

    private String extraInfo;

}
