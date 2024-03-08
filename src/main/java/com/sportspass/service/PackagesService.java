package com.sportspass.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportspass.controller.RequestTokenController;
import com.sportspass.dms.Packages;
import com.sportspass.repository.PackagesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PackagesService {
    private static final Logger logger = LoggerFactory.getLogger(RequestTokenController.class);

    @Autowired
    PackagesRepository packagesRepository;

    public ResponseEntity<String> listPackages() {
        List<Packages> listPackages = packagesRepository.findAllByActiveTrue();
        Map<String, Object> response = new HashMap<>();
        response.put("packages", listPackages);
        return objectMapperResponse(response);
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
