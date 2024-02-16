package com.sportspass.dms;


import com.sportspass.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "request_tokens")
@RequiredArgsConstructor
public class RequestToken {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable=true)
    private String status; //accept{idPartner}, or refuse

    @Column(nullable = true)
    private String page;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "partner_id")
    private User partnerId;


    @ManyToOne
    @JoinColumn(name = "account_user_id")
    private AccountUser accountUser;


}
