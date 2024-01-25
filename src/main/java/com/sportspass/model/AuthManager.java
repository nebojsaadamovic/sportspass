package com.sportspass.model;

import com.sportspass.repository.UserRepository;
import com.sportspass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


@Component
public class AuthManager implements AuthenticationManager {

    @Autowired
    UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            String username = authentication.getName();
            String password = authentication.getCredentials().toString();

            User user=userService.getUserByUsernameAndPassword(username,password);
            // Replace this with your actual authentication logic (e.g., check against a database)
            if (user!=null) {
                // If authentication is successful, return an authenticated token
                return new UsernamePasswordAuthenticationToken(username, password, authentication.getAuthorities());
            } else {
                // If authentication fails, throw an AuthenticationException
                throw new BadCredentialsException("Invalid username or password");
            }
        }



}
