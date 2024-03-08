package com.sportspass.repository;
import com.sportspass.dms.AccountPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountPartnerRepository extends JpaRepository<AccountPartner,Long> {

    @Query(value = "SELECT * FROM account_partners ap WHERE ap.partner_id = :partnerId", nativeQuery = true)
    AccountPartner findAccountPartnerByPartner(Long partnerId);
}
