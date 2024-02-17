package com.sportspass.service;

import com.sportspass.dms.AccountUser;
import com.sportspass.dms.RequestToken;
import com.sportspass.model.User;
import com.sportspass.repository.RequestTokenRepository;
import com.sportspass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RequestTokenService {


    @Autowired
    RequestTokenRepository requestTokenRepository;
    @Autowired
    UserRepository userRepository;

    public void save(Long userId, String randomRequestToken, String status, String page, AccountUser accountUser, Date date) {
        // Assuming you have a method to retrieve User details from the userId
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            RequestToken requestToken = new RequestToken();
            requestToken.setUser(user);
            requestToken.setToken(randomRequestToken);
            requestToken.setStatus(status);
            requestToken.setPage(page);
            requestToken.setAccountUser(accountUser);
            requestToken.setDateGenerateQR(date);
            requestTokenRepository.save(requestToken);
        } else {
            // Handle the case where the user with the given userId is not found
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
    }

    public RequestToken findRequestTokenByToken(String token) {
        return requestTokenRepository.findRequestTokenByToken(token);
    }

    public void updateTokenAndStatusAndPartner(Long userId, String status, User partnerIdLong) {
        requestTokenRepository.updateTokenAndStatusAndPartner(userId, status, partnerIdLong);
    }

    public RequestToken getReferenceById(Long id) {
        return requestTokenRepository.getReferenceById(id);
    }

    public RequestToken save(RequestToken requestToken) {
        return requestTokenRepository.save(requestToken);
    }
}
