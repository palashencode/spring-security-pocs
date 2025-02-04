package com.java.app.usermgmt.repository;

import com.java.app.usermgmt.entity.User;
import com.java.app.usermgmt.entity.UserRoles;
import org.springframework.data.repository.CrudRepository;

public interface UserRolesRepository extends CrudRepository<UserRoles, Long> {

}
