package com.flipkart.exceptions;

public class UserNotFoundException extends Exception {
    private String email;
    
    public UserNotFoundException() {
        super("User not found");
    }
    
    public UserNotFoundException(String email) {
        super("User not found: " + email);
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }
    
    @Override
    public String getMessage() {
        return email != null ? "User not found: " + email : "User not found";
    }
}
