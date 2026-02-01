package com.flipkart.exceptions;

public class NoDataFoundException extends Exception {
    public String getMessage() {
        return "No data found";
    }
}
