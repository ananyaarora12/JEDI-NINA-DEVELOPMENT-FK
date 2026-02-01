package com.flipkart.exceptions;

public class GymOwnerNotFoundException extends Exception {
    public String getMessage() {
        return "Gym owner not found";
    }
}
