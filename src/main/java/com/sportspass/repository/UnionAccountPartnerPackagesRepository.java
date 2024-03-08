package com.sportspass.repository;

import com.sportspass.dms.UnionAccountPartnerPackages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UnionAccountPartnerPackagesRepository extends JpaRepository<UnionAccountPartnerPackages, Long> {

    @Query(value = "SELECT  uapp.id, uapp.account_partner_id, uapp.package_id, uapp.active " +
            "FROM union_account_partner_packages AS uapp " +
            "LEFT JOIN account_partners AS ap ON uapp.account_partner_id = ap.id " +
            "LEFT JOIN packages AS p ON uapp.package_id = p.id " +
            "LEFT JOIN users AS u ON ap.partner_id = u.id " +
            "WHERE u.id = ?1 AND p.id = ?2 AND uapp.active = 1", nativeQuery = true)
    UnionAccountPartnerPackages findUnionAccountPartnerPackagesByPartnerAndPackage(Long partnerId, Long packagesId);

}
