package com.sportspass.dms;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "union_account_user_packages")
@RequiredArgsConstructor
public class UnionAccountUserPackages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_user_id")
    private AccountUser accountUser;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Packages packages;

    @Column(name = "date_purchase",nullable = false, unique = true)
    private Date datePurchase;

    @Column(name = "date_expiry",nullable = false, unique = true)
    private Date dateExpiry;

    // Dodatni atributi koji su specifični za vezu između AccountUser i Packages

    // Konstruktori, getteri i setteri
}
