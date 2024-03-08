package com.sportspass.controller;

import com.sportspass.service.AccountUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/account-user/")
@Transactional
public class AccountUserController {

    @Autowired
    AccountUserService accountUserService;


    @PostMapping("add-account")
    public ResponseEntity<String> addAccount(@RequestBody Map<String, Object> requestMap) {
        try {
            String response = accountUserService.addAccount(requestMap);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }


}
