package com.sportspass.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportspass.dms.*;
import com.sportspass.model.User;
import com.sportspass.repository.*;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class RequestTokenService {
    ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(RequestTokenService.class);
    @Autowired
    RequestTokenRepository requestTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountUserService accountUserService;
    @Autowired
    PackagesRepository packagesRepository;
    @Autowired
    AccountUserRepository accountUserRepository;
    @Autowired
    AccountPartnerRepository accountPartnerRepository;
    @Autowired
    UnionAccountPartnerPackagesRepository unionAccountPartnerPackagesRepository;

    public ResponseEntity<String> createRequestToken(@RequestBody Map<String, Object> requestMap) {
        Long userId = Long.valueOf(requestMap.get("userId").toString());
        String status = (String) requestMap.get("status");
        String page = (String) requestMap.get("page");
        Integer trimmedPackage = (Integer) requestMap.get("trimmedPackage");
        String token = generateRandomString();

        User user = userRepository.getReferenceById(userId);
        AccountUser accountUser = accountUserRepository.findByUserIdByPackageAAndActive(user.getId(), Long.valueOf(trimmedPackage), true);
        Date date = new Date();
        save(user.getId(), token, status, page, accountUser, date);
        RequestToken requestToken = requestTokenRepository.findRequestTokenByToken(token);

        Map<String, Object> response = new HashMap<>();
        response.put("userName", requestToken.getUser().getUsername());
        response.put("token", requestToken.getToken());
        response.put("status", requestToken.getStatus());
        if (accountUser != null) {
            response.put("package", accountUser.getPackageA() != null ? accountUser.getPackageA() : null);
            response.put("numberOfEntries", accountUser.getNumberOfEntries() != null ? accountUser.getNumberOfEntries() : null);
        }
        return objectMapperResponse(response);
    }

    public void save(Long userId, String randomRequestToken, String status, String page, AccountUser accountUser, Date date) {
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
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
    }

    @Transactional
    public String readRequestToken(@RequestBody Map<String, Object> requestMap) throws JsonProcessingException {
        String status = (String) requestMap.get("status");
        String token = (String) requestMap.get("token");
        Long partnerId = Long.valueOf(requestMap.get("partnerId").toString());
        Long packageTrim = Long.valueOf(requestMap.get("packageTrim").toString());
        Map<String, Object> response = new HashMap<>();

        UnionAccountPartnerPackages unionAccountPartnerPackages = unionAccountPartnerPackagesRepository.findUnionAccountPartnerPackagesByPartnerAndPackage(partnerId, packageTrim);
        if (ObjectUtils.isEmpty(unionAccountPartnerPackages)) {
            Packages packagesName = packagesRepository.getReferenceById(packageTrim);
            response.put("greska", "Partner ne posjeduje " + packagesName.getName() + " paket!");
            return objectMapper.writeValueAsString(response);
        }
        RequestToken requestToken = requestTokenRepository.findRequestTokenByToken(token);
        if (requestToken == null) {
            response.put("greska", "greska");
            return objectMapper.writeValueAsString(response);
        }
        if (Objects.equals(requestToken.getStatus(), "QR code ucitan")) {
            response.put("greska", "QR code je vec jednom ucitan!");
            return objectMapper.writeValueAsString(response);
        }
        int numEntries;
        User user = requestToken.getUser();
        User partnerIdlong = userRepository.getReferenceById(partnerId);
        AccountUser accountUser = accountUserRepository.findByUserIdByPackageAAndActive(user.getId(), packageTrim, true);
        if (accountUser.getNumberOfEntries() < 1) {
            return responseRequestToken(status, partnerIdlong, accountUser, token, response, 0, null, null);
        }
        numEntries = accountUser.getNumberOfEntries() - 1;
        accountUser.setNumberOfEntries(numEntries);

        Packages packageName = unionAccountPartnerPackages.getPackages();
        AccountPartner accountPartner = accountPartnerRepository.findAccountPartnerByPartner(partnerIdlong.getId());
        Double currentBalance = accountPartner.getTotalMoney();
        Double ticketPrice = packageName.getTicketPrice();
        Double currentBalanceAddTicketPrice = currentBalance + ticketPrice;
        accountPartner.setTotalMoney(currentBalanceAddTicketPrice);
        accountPartnerRepository.saveAndFlush(accountPartner);

        return responseRequestToken(status, partnerIdlong, accountUser, token, response, numEntries, ticketPrice, currentBalanceAddTicketPrice);
    }

    public String responseRequestToken(String status, User partnerIdlong, AccountUser accountUser, String token, Map<String, Object> response, int numEntries, Double ticketPrice, Double currentBalance) throws JsonProcessingException {
        Integer countEntries = numEntries;
        requestTokenRepository.updateTokenAndStatusAndPartner(accountUser.getUser().getId(), status, partnerIdlong, token, countEntries);
        accountUserRepository.save(accountUser);
        RequestToken requestTokenUpdate = requestTokenRepository.findRequestTokenByToken(token);
        User userName = userRepository.getReferenceById(requestTokenUpdate.getUser().getId());

        RequestToken requestToken1 = requestTokenRepository.getReferenceById(requestTokenUpdate.getId());
        requestToken1.setAccountPartner(accountUser);
        requestToken1.setDateReadQR(new Date());
        requestToken1.setPartnerId(partnerIdlong);
        requestToken1.setTicketPrice(ticketPrice);
        requestToken1.setCurrentBalance(currentBalance);
        requestTokenRepository.save(requestToken1);

        response.put("userName", userName.getUsername());
        response.put("firstName", userName.getFirstName());
        response.put("lastName", userName.getLastName());
        response.put("status", requestTokenUpdate.getStatus());
        return objectMapper.writeValueAsString(response);
    }

    public ResponseEntity<String> readRequestTokenForGenerateQR(@RequestBody Map<String, Object> requestMap) {//run every 3 sec
        Long userId = Long.valueOf(requestMap.get("userId").toString());
        String token = (String) requestMap.get("token");
        User user = userRepository.getReferenceById(userId);
        RequestToken requestTokenUpdate = requestTokenRepository.findRequestTokenByToken(token);
        String trimmedPackageObject = (String) requestMap.get("trimmedPackage");

        if (trimmedPackageObject == null) {
            System.out.println("Value for key 'trimmedPackage' is null");
            return null;
        }
        Map<String, Object> response = new HashMap<>();
        User partner = null;
        if (requestTokenUpdate != null) {
            if (requestTokenUpdate.getPartnerId() != null) {
                partner = requestTokenUpdate.getPartnerId();
            }
        }
        AccountUser accountUser = accountUserRepository.findByUserIdByPackageAAndActive(user.getId(), Long.valueOf(trimmedPackageObject), true);
        List<AccountUser> listAccountUsers = accountUserRepository.findAllByUserIdAndActive(user.getId(), true);
        List<Packages> packagesList = new ArrayList<>();

        if (ObjectUtils.isEmpty(accountUser)) {
            response.put("greska", "Potroseni su termini " + user.getUsername());
            return objectMapperResponse(response);
        }
        for (AccountUser accountUser1 : listAccountUsers) {
            Long packageId = Long.valueOf(accountUser1.getPackageA());
            Packages objectPackages = packagesRepository.getReferenceById(packageId);
            Packages actualPackage = (Packages) Hibernate.unproxy(objectPackages);
            packagesList.add(actualPackage);
        }
        if (ObjectUtils.isEmpty(accountUser)) {
            response.put("greska", "Paket je prazan. Kupite novi paket!");
            return objectMapperResponse(response);
        }
        if (partner == null) {
            response.put("userName", user.getUsername());
        } else {
            response.put("userName", requestTokenUpdate.getPartnerId().getUsername());
        }
        Packages packages = packagesRepository.getReferenceById(Long.valueOf(trimmedPackageObject));
        response.put("packageList", packagesList);
        response.put("status", requestTokenUpdate != null ? requestTokenUpdate.getStatus() : null);
        response.put("page", requestTokenUpdate != null ? requestTokenUpdate.getPage() : null);
        response.put("package", packages.getName());
        if (accountUser.getNumberOfEntries() == 0) {
            accountUser.setActive(false);
        }
        response.put("numberOfEntries", accountUser.getNumberOfEntries() != null ? accountUser.getNumberOfEntries() : null);
        if (partner != null) {
            response.put("partnerId", partner.getId());
        }
        return objectMapperResponse(response);
    }

    private static String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomStringBuilder = new StringBuilder(25);
        Random random = new Random();
        for (int i = 0; i < 25; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            randomStringBuilder.append(randomChar);
        }
        return randomStringBuilder.toString();
    }

    private ResponseEntity<String> objectMapperResponse(Map<String, Object> response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(response);
            return ResponseEntity.ok(jsonResponse);
        } catch (JsonProcessingException e) {
            logger.error("Error converting response to JSON:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting response to JSON");
        }
    }


}
