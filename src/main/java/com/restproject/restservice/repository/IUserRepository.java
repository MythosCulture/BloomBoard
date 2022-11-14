package com.restproject.restservice.repository;

import com.restproject.restservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IUserRepository extends JpaRepository<User, Long> {

    //@Query("SELECT * FROM users WHERE name = userName")
    List<User> findByName(String name);
}
