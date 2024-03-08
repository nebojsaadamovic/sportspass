package com.sportspass.dms;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "union_account_partner_packages")
@RequiredArgsConstructor
public class UnionAccountPartnerPackages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_partner_id")
    private AccountPartner accountPartner;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Packages packages;

    @Column(name = "active", nullable = true)
    private Boolean active;


    public Long id() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountPartner accountPartner() {
        return accountPartner;
    }

    public void setAccountPartner(AccountPartner accountPartner) {
        this.accountPartner = accountPartner;
    }

    public Packages packages() {
        return packages;
    }

    public void setPackages(Packages packages) {
        this.packages = packages;
    }

    public Boolean active() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }



// Constructors, getters, and setters
}
