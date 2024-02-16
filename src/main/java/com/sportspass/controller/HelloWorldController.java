package com.sportspass.controller;

import com.sportspass.model.User;
import com.sportspass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@SuppressWarnings("ALL")
@RestController
@RequestMapping("/api/v3/")
public class HelloWorldController {


    @Autowired
    private UserService userService;









}
