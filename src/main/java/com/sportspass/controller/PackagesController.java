package com.sportspass.controller;


import com.sportspass.service.PackagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/packages/")
@Transactional
public class PackagesController {

    @Autowired
    PackagesService packagesService;

    @GetMapping("list-packages")
    public ResponseEntity<String> getAllPackages() {
        return packagesService.listPackages();
    }


}