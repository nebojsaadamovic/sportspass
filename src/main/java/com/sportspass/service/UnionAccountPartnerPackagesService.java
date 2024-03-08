package com.sportspass.service;


import com.sportspass.repository.UnionAccountPartnerPackagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class UnionAccountPartnerPackagesService {

    @Autowired
    UnionAccountPartnerPackagesRepository unionAccountPartnerPackagesRepository;

}
