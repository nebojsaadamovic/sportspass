package com.sportspass.model;

public class AuthenticationResponse {

    private String jwtToken;

    // Constructors, getters, and setters

    public AuthenticationResponse() {
        // Default constructor
    }

    public AuthenticationResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
