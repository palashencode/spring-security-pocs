package com.java.app.usermgmt.repository;

import com.java.app.usermgmt.entity.User;
import com.java.app.usermgmt.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
        List<UserData> findByUserId(Integer userId);
}
