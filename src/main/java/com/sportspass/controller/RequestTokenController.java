package com.sportspass.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/api/request-token/")
@Transactional
public class RequestTokenController {
    private static final Logger logger = LoggerFactory.getLogger(RequestTokenController.class);
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

    @PostMapping("create-request-token") // generate qr code
    public ResponseEntity<String> createRequestTokenEndpoint(@RequestBody Map<String, Object> requestMap) {
        return requestTokenService.createRequestToken(requestMap);
    }

    @PostMapping("read-request-token")
    @Transactional
    public ResponseEntity<String> readRequestToken(@RequestBody Map<String, Object> requestMap) {
        try {
            String response = requestTokenService.readRequestToken(requestMap);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    @PostMapping("read-request-token-generate") //run every 3 sec
    public ResponseEntity<String> readRequestTokenForGenerateQR(@RequestBody Map<String, Object> requestMap) {
        return requestTokenService.readRequestTokenForGenerateQR(requestMap);
    }


}
