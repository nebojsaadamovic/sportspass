package com.sportspass.dms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "packages")
@RequiredArgsConstructor
public class Packages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String name;


    @Column(name = "number_of_entries",nullable = false)
    private Integer numberOfEntries;

    @Column(name = "price",nullable = false)
    private Integer price;

    @Column(name = "active",nullable = false)
    private Boolean active=true;


    @Column(name = "ticket_price",nullable = true)
    private Double ticketPrice;


    @JsonIgnore // Add this annotation to ignore serialization of this field
    @OneToMany(mappedBy = "packages", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UnionAccountUserPackages> accountUsers;



//    @JsonIgnore // Add this annotation to ignore serialization of this field
//    @OneToMany(mappedBy = "packages", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<UnionAccountPartnerPackages> accountPartners;

    @Override
    public String toString() {
        return "Packages{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numberOfEntries=" + numberOfEntries +
                ", price=" + price +
                ", active=" + active +
                '}';
    }
}
