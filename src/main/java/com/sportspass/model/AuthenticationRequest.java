package com.sportspass.model;



public class AuthenticationRequest {

    private String username;
    private String password;

    // Constructors, getters, and setters

    public AuthenticationRequest() {
        // Default constructor
    }

    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters for username and password

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
