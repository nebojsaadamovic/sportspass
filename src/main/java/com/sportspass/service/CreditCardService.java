package com.sportspass.service;


import com.sportspass.dms.CreditCard;
import com.sportspass.model.User;
import com.sportspass.repository.CreditCardRepository;
import com.sportspass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Service
public class CreditCardService {

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<String> createCard(@RequestBody Map<String, Object> requestMap, @PathVariable String userId) {
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber((String) requestMap.get("cardNumber"));
        creditCard.setCardholderName((String) requestMap.get("cardHolderName"));
        creditCard.setCvv((String) requestMap.get("cvv"));
        String expiryDateMonth = (String) requestMap.get("expiryDate");
        String monthString = expiryDateMonth.substring(0, 2);
        int expirationMonth = Integer.parseInt(monthString);
        creditCard.setExpirationMonth(expirationMonth);
        String expiryDateYear = (String) requestMap.get("expiryDate");
        String yearString = expiryDateYear.substring(expiryDateYear.length() - 2);
        int expirationYear = Integer.parseInt(yearString);
        creditCard.setExpirationYear(expirationYear);
        creditCard.setBankName((String) requestMap.get("bankName"));
        CreditCard savedCreditCard = save(creditCard, Long.valueOf(userId));

        if (savedCreditCard != null) {
            return ResponseEntity.ok("Credit card registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register credit card");
        }
    }

    public CreditCard save(CreditCard creditCard, Long userId) {
        User user = userRepository.getReferenceById(userId);
        creditCard.setUser(user);
        return creditCardRepository.save(creditCard);
    }

    public List<CreditCard> listCardsByUser(Long userId) {
        return creditCardRepository.listAllCreditCardsByUser(userId);
    }

}
