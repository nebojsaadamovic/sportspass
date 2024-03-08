package com.sportspass.controller;

import com.sportspass.model.User;
import com.sportspass.repository.PackagesRepository;
import com.sportspass.repository.UserRepository;
import com.sportspass.service.AccountUserService;
import com.sportspass.service.RequestTokenService;
import com.sportspass.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/user/")
@Transactional
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(AccountUserController.class);

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RequestTokenService requestTokenService;
    @Autowired
    AccountUserService accountUserService;
    @Autowired
    PackagesRepository packagesRepository;


    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        return userService.loginUser(loginRequest);
    }

    @PostMapping("register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("partners")
    public ResponseEntity<List<String>> listPartners() {
        List<String> partners = userService.listPartners();
        return ResponseEntity.ok(partners);
    }

    @PostMapping("list-used-terms-packages/{userId}")
    public ResponseEntity<List<String>> listOfUsedTermsByPackages(@PathVariable String userId) {
        try {
            List<String> usedTerms = userService.listOfUsedTermsByPackages(Long.valueOf(userId));
            return ResponseEntity.ok(usedTerms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList("Error occurred: " + e.getMessage()));
        }
    }




}