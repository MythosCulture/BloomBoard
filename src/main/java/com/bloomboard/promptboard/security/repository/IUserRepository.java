package com.bloomboard.promptboard.security.repository;

import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.security.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface IUserRepository extends JpaRepository<User, Long> {
    List<User> findByUserRole(UserRole userRole);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameIgnoreCase(String username);
    //List<User> findByName(String name);
}
