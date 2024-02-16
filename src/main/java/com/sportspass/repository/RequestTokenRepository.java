package com.sportspass.repository;

import com.sportspass.dms.RequestToken;

import com.sportspass.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestTokenRepository extends JpaRepository <RequestToken,Long>{


    @Query("SELECT rt.status, rt.token, u.username AS user_username, p.username AS partner_username " +
            "FROM RequestToken rt " +
            "LEFT JOIN rt.user u " +
            "LEFT JOIN rt.partnerId p " +
            "WHERE rt.token = :token")
    RequestToken getRequestTokenDetailsByToken(@Param("token") String token);

    
    @Query("SELECT rt FROM RequestToken rt WHERE rt.token = :token")
    RequestToken findRequestTokenByToken(@Param("token") String token);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE RequestToken rt SET  rt.status = :status, rt.partnerId = :partnerId " +
            "WHERE rt.user.id = :userId")
    void updateTokenAndStatusAndPartner(Long userId, String status, User partnerId);


}
