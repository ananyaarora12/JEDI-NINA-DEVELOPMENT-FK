package com.flipkart.exceptions;

public class GymCenterDetailsNotFoundException extends Exception {
	public String getMessage() {
        return "Oops! GymDetails not found";
    }

}
