package com.sportspass.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportspass.DTO.UnionAccountPackageDTO;
import com.sportspass.controller.AccountUserController;
import com.sportspass.dms.AccountUser;
import com.sportspass.dms.Packages;
import com.sportspass.dms.UnionAccountUserPackages;
import com.sportspass.model.User;
import com.sportspass.repository.AccountUserRepository;
import com.sportspass.repository.PackagesRepository;
import com.sportspass.repository.UnionAccountUserPackageRepository;
import com.sportspass.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class AccountUserService {
    private static final Logger logger = LoggerFactory.getLogger(AccountUserController.class);

    @Autowired
    PackagesRepository packagesRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UnionAccountUserPackageRepository unionAccountUserPackageRepository;
    @Autowired
    AccountUserRepository accountUserRepository;

    @PostMapping("add-account")
    public String addAccount(@RequestBody Map<String, Object> requestMap) throws JsonProcessingException {
        Packages packageObj = packagesRepository.getReferenceById(Long.valueOf(requestMap.get("packageId").toString()));
        User user = userRepository.getReferenceById(Long.valueOf(requestMap.get("userId").toString()));
        AccountUser accountUserExist = accountUserRepository.findByUserIdByPackageAAndActive(user.getId(), packageObj.getId(), true);

        UnionAccountPackageDTO responseDTO = new UnionAccountPackageDTO();
        if (accountUserExist != null) {
            responseDTO.setMessage("Imate isti paket sa neiskoristenim terminima!");
        } else {
            AccountUser accountUser = new AccountUser();
            accountUser.setUser(user);
            accountUser.setNumberOfEntries(packageObj.getNumberOfEntries());
            accountUser.setCountEntries(packageObj.getNumberOfEntries());
            accountUser.setPayment(String.valueOf(packageObj.getPrice()));
            accountUser.setPackageA(String.valueOf(packageObj.getId()));
            accountUser.setActive(true);
            accountUserRepository.save(accountUser);

            UnionAccountUserPackages unionAccountUserPackages = new UnionAccountUserPackages();
            unionAccountUserPackages.setAccountUser(accountUser);
            unionAccountUserPackages.setPackages(packageObj);
            unionAccountUserPackages.setDatePurchase(new Date());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(unionAccountUserPackages.getDatePurchase());
            calendar.add(Calendar.MONTH, 1);
            unionAccountUserPackages.setDateExpiry(calendar.getTime());

            UnionAccountUserPackages accountUserPackagesSave = unionAccountUserPackageRepository.save(unionAccountUserPackages);
            UnionAccountUserPackages unionAccountUserPackages1 = unionAccountUserPackageRepository.findPackageAndAccountUserByIdUnion(unionAccountUserPackages.getId());

            responseDTO.setPackageName(unionAccountUserPackages1.getPackages().getName());
            responseDTO.setNumberOfEntries(unionAccountUserPackages1.getPackages().getNumberOfEntries());
            responseDTO.setPrice(unionAccountUserPackages1.getPackages().getPrice());
            responseDTO.setDatePurchase(unionAccountUserPackages1.getDatePurchase());
            responseDTO.setDateExpiry(unionAccountUserPackages1.getDateExpiry());
            if (accountUserPackagesSave == null) {
                responseDTO.setMessage("Kupovina nije uspjela !!!");
            } else {
                responseDTO.setMessage("Uspjesno obavljena kupovina !!!");
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(responseDTO);
    }

}