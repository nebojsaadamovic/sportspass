package com.sportspass.controller;

import com.sportspass.model.Role;
import com.sportspass.model.User;
import com.sportspass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("random")
    public Map<String, Integer> getRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(100);
        // Create a Map to represent the JSON structure
        Map<String, Integer> response = new HashMap<>();
        response.put("randomNumber", randomNumber);
        System.out.println(response);
        return response;
    }


    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();
        User user = userService.getUserByUsernameAndPassword(username, password);
        String roleNames = null;
        if (user != null) {
            Set<Role> roles = user.roles();

            for (Role role : roles) {
                roleNames = role.name();
            }
        }
       // System.out.println(roleNames);
        if (roleNames.equals("USER")) {
            return ResponseEntity.ok("USER");
        }
        if (roleNames.equals("PARTNER")) {
            return ResponseEntity.ok("PARTNER");
        }
        // Here you can generate a JWT token or perform other authentication-related operations
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        }

    }



    @PostMapping("register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        System.out.println("Received user registration request: " + user);
        User registrationSuccess =  userService.save(user);
        if (registrationSuccess != null) {
            System.out.println(registrationSuccess);
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
        }
    }



}



