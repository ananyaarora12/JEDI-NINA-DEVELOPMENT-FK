package com.flipkart.exceptions;

public class NoSlotsFoundException extends Exception {
    public String getMessage() {
        return "No slot found";
    }
}
