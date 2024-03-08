package com.sportspass.repository;

import com.sportspass.dms.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    @Query(value = "SELECT * FROM credit_cards c WHERE c.user_id = :userId", nativeQuery = true)
    List<CreditCard> listAllCreditCardsByUser(@Param("userId") Long userId);

}

