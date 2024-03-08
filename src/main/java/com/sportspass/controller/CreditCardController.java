package com.sportspass.controller;

import com.sportspass.dms.CreditCard;
import com.sportspass.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/credit-card/")
public class CreditCardController {

    @Autowired
    CreditCardService creditCardService;

    @PostMapping("create/{userId}")
    public ResponseEntity<String> createCard(@RequestBody Map<String, Object> requestMap, @PathVariable String userId) {
        return creditCardService.createCard(requestMap, userId);
    }

    @GetMapping("list-credit-cards/{userId}")
    public ResponseEntity<List<CreditCard>> listCreditCardsByUser(@PathVariable Long userId) {
        List<CreditCard> creditCards = creditCardService.listCardsByUser(userId);
        return ResponseEntity.ok(creditCards);
    }


}
