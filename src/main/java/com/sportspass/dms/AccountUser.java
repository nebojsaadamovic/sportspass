package com.sportspass.dms;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "accountUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RequestToken> requestTokens;

    @Column(nullable = true)
    private String payment;

    @Column(name = "count_entries",nullable = true)
    private Integer countEntries;

    @Column(name = "number_of_entries",nullable = true)
    private Integer numberOfEntries;   //obrisati

    @Column(name="package_a", nullable = true)
    private String packageA;

    @Column(nullable = true)
    private Boolean active;

    @JsonIgnore // Add this annotation to ignore serialization of this field
    @OneToMany(mappedBy = "accountUser")
    private List<UnionAccountUserPackages> packages;





    public User user() {
        return user;
    }

    @Override
    public String toString() {
        return "AccountUser{" +
                "id=" + id +
                ", user=" + user +
                ", payment='" + payment + '\'' +
                ", packageA='" + packageA + '\'' +
                ", active=" + active +
                // Avoid calling toString() on related entities to prevent potential recursion
                '}';
    }
}
