package com.sportspass.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportspass.dms.RequestToken;
import com.sportspass.model.Role;
import com.sportspass.model.User;
import com.sportspass.repository.UserRepository;
import com.sportspass.service.AccountUserService;
import com.sportspass.service.RequestTokenService;
import com.sportspass.service.UserService;
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

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RequestTokenService requestTokenService;
    @Autowired
    AccountUserService accountUserService;

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        User user = userService.getUserByUsernameAndPassword(username, password);
        Map<String, String> response = new HashMap<>();
        String roleNames = null;
        if (user != null) {
            Set<Role> roles = user.getRoles();
            for (Role role : roles) {
                roleNames = role.getName();
            }
        }
        if (roleNames.equals("USER")) {
            String userId = String.valueOf(user.getId());
            response.put("role", "USER");
            response.put("userId", userId);
            return ResponseEntity.ok(response.toString());
        }
        if (roleNames.equals("PARTNER")) {
            String userId = String.valueOf(user.getId());
            response.put("role", "PARTNER");
            response.put("userId", userId);
            return ResponseEntity.ok(response.toString());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        }
    }

    @PostMapping("register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        System.out.println("Received user registration request: " + user);
        User registrationSuccess = userService.save(user);
        if (registrationSuccess != null) {
            System.out.println(registrationSuccess+"lll");
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
        }
    }


    @GetMapping("partners")
    public ResponseEntity<List<String>> listPartners() {
        List<String> partners = userService.listPartners();
        return ResponseEntity.ok(partners);
    }

}