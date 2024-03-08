package com.sportspass.controller;

import com.sportspass.model.User;
import com.sportspass.repository.UserRepository;
import com.sportspass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/partner/")
@Transactional
public class PartnerController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // return partnerService.save(user); //TODO FINISH REGISTRATION PARTNER
        return null;
    }

    @PostMapping("list-used-terms-packages/{partnerId}")
    public ResponseEntity<List<String>> listOfUsedTermsByPackagesByPartner(@PathVariable String partnerId) {
        try {
            List<String> usedTerms = userService.listOfUsedTermsByPackagesAndPartner(Long.valueOf(partnerId));
            System.out.println(usedTerms);
            return ResponseEntity.ok(usedTerms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList("Error occurred: " + e.getMessage()));
        }
    }


}
