package com.sportspass.repository;

import com.sportspass.dms.RequestToken;
import com.sportspass.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT rt.status, rt.token, u.username AS user_username, p.username AS partner_username " +
            "FROM request_tokens rt " +
            "LEFT JOIN users u ON u.id = rt.user_id " +
            "LEFT JOIN users p ON p.id = rt.partner_id " +
            "WHERE rt.token = :token", nativeQuery = true)
    RequestToken getRequestTokenDetailsByToken(@Param("token") String token);

    User findByUsernameAndPassword(String username, String password);

    @Query("SELECT DISTINCT u.username FROM User u JOIN u.roles r WHERE r.name = 'PARTNER'")
    List<String> getPartners();




}
