package com.sportspass.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportspass.controller.AccountUserController;
import com.sportspass.dms.AccountUser;
import com.sportspass.dms.Packages;
import com.sportspass.model.Role;
import com.sportspass.model.User;
import com.sportspass.repository.AccountUserRepository;
import com.sportspass.repository.PackagesRepository;
import com.sportspass.repository.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AccountUserController.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    RequestTokenService requestTokenService;
    @Autowired
    AccountUserService accountUserService;
    @Autowired
    PackagesRepository packagesRepository;
    @Autowired
    AccountUserRepository accountUserRepository;

    public ResponseEntity<String> loginUser(User loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        User user = userRepository.findByUsernameAndPassword(username, password);
        Map<String, String> response = new HashMap<>();
        String roleNames = null;
        if (user != null) {
            Set<Role> roles = user.getRoles();
            for (Role role : roles) {
                roleNames = role.getName();
            }
        }
        assert roleNames != null;
        if (roleNames.equals("USER")) {
            String userId = String.valueOf(user.getId());
            List<AccountUser> listAccountUsers = accountUserRepository.findAllByUserIdAndActive(user.getId(), true);
            List<String> packagesList = new ArrayList<>();

            for (AccountUser accountUser1 : listAccountUsers) {
                Long packageId = Long.valueOf(accountUser1.getPackageA());
                Packages objectPackages = packagesRepository.getReferenceById(packageId);
                Packages actualPackage = (Packages) Hibernate.unproxy(objectPackages);// Ensure you're working with the actual entity object rather than a proxy // Trigger lazy loading to fetch the actual object
                packagesList.add(String.valueOf(actualPackage.getId()));
            }
            response.put("packagesList", packagesList.toString());
            response.put("role", "USER");
            response.put("userId", userId);
            return objectMapperResponse(response);
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

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<String> listPartners() {
        return userRepository.getPartners();
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public ResponseEntity<String> save(User user) {
        User registrationSuccess = userRepository.save(user);
        if (!ObjectUtils.isEmpty(registrationSuccess)) {
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
        }
    }

    private ResponseEntity<String> objectMapperResponse(Map<String, String> response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(response);
            return ResponseEntity.ok(jsonResponse);
        } catch (JsonProcessingException e) {
            logger.error("Error converting response to JSON:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting response to JSON");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public List<String> listOfUsedTermsByPackages(Long userId) {
        return userRepository.getListOfUsedTermsByPackages(userId);
    }

    public List<String> listOfUsedTermsByPackagesAndPartner(Long partnerId) {
        return userRepository.getListOfUsedTermsByPackagesAndPartner(partnerId);
    }


}
