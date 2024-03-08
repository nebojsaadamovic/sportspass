package com.sportspass.dms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sportspass.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Entity
@Table(name = "account_partners")
@RequiredArgsConstructor
public class AccountPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "partner_id")
    private User partner;


    @Column(name = "total_money")
    private Double totalMoney;

    @JsonIgnore // Add this annotation to ignore serialization of this field
    @OneToMany(mappedBy = "accountPartner")
    private List<UnionAccountPartnerPackages> packages;

    public Long id() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPartner() {
        return partner;
    }

    public void setPartner(User partner) {
        this.partner = partner;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public List<UnionAccountPartnerPackages> packages() {
        return packages;
    }

    public void setPackages(List<UnionAccountPartnerPackages> packages) {
        this.packages = packages;
    }


}
