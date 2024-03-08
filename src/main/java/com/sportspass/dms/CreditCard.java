package com.sportspass.dms;

import com.sportspass.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

// CreditCard class

@Data
@Entity
@Table(name = "credit_cards")
@RequiredArgsConstructor
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "card_number",nullable = false, unique = true)
    private String cardNumber;

    @Column(name = "card_holder_name",nullable = false)
    private String cardholderName;

    @Column(name = "expiration_month",nullable = false)
    private int expirationMonth;

    @Column(name = "expiration_year",nullable = false)
    private int expirationYear;

    @Column(nullable = false)
    private String cvv;

    @Column(name = "bank_name",nullable = false)
    private String bankName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToOne
//    @JoinColumn(name = "account_user_id")
//    private AccountUser accountUser;


}
