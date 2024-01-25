package com.sportspass.repository;

import com.sportspass.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Additional custom queries can be added if needed
    User findByUsernameAndPassword(String username, String password);

}
