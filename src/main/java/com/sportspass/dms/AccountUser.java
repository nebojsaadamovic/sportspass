package com.sportspass.dms;

import com.sportspass.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "account_users")
@RequiredArgsConstructor
public class AccountUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


//    @OneToOne(mappedBy = "accountUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private CreditCard creditCard;

    @OneToMany(mappedBy = "accountUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RequestToken> requestTokens;

    @Column(nullable = true)
    private String payment;

    @Column(nullable = true)
    private Integer numberOfEntries;

    @Column(nullable = true)
    private String packageA;

    @Column(nullable = true)
    private Boolean active;


}
