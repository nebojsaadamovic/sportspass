package com.sportspass.repository;

import com.sportspass.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndPassword(String username, String password);

    @Query("SELECT DISTINCT u.username FROM User u JOIN u.roles r WHERE r.name = 'PARTNER'")
    List<String> getPartners();

    @Transactional
    @Query(value = "SELECT " +
            "p.name as paket, " +
            "rt.count_entries, " +
            "DATE_FORMAT(rt.date_read_qr, '%d-%m-%Y %H:%i:%s') AS date_read_qr, " +
            "rt.status, " +
            "partner.user_name as partnername " +
            "FROM request_tokens AS rt " +
            "LEFT JOIN users AS u ON rt.user_id = u.id " +
            "LEFT JOIN users AS partner ON rt.partner_id = partner.id "+
            "LEFT JOIN account_users AS au ON rt.account_user_id = au.id " +
            "LEFT JOIN packages AS p ON au.package_a= p.id " +
            "WHERE " +
            "au.active " +
            "AND rt.status = 'QR code ucitan' " +
            "AND u.id = :userId " +
            "ORDER BY " +
            "au.id, rt.date_read_qr", nativeQuery = true)
    List<String> getListOfUsedTermsByPackages(Long userId);

    @Transactional
    @Query(value = "SELECT " +
            "p.name as paket, " +
            "DATE_FORMAT(rt.date_read_qr, '%d-%m-%Y %H:%i:%s') AS date_read_qr, " +
            "rt.status, " +
            "u.first_name as firstName, " +
            "u.last_name as lastName, " +
            "rt.ticket_price as ticket, " +
            "rt.current_balance as currentBalance " +
            "FROM request_tokens AS rt " +
            "LEFT JOIN users AS u ON rt.user_id = u.id " +
            "LEFT JOIN users AS partner ON rt.partner_id = partner.id "+
            "LEFT JOIN account_users AS au ON rt.account_user_id = au.id " +
            "LEFT JOIN packages AS p ON au.package_a= p.id " +
            "WHERE " +
            "rt.status = 'QR code ucitan' " +
            "AND partner.id = :partnerId " +
            "ORDER BY " +
            "rt.date_read_qr DESC ", nativeQuery = true)
    List<String> getListOfUsedTermsByPackagesAndPartner(Long partnerId);


}
