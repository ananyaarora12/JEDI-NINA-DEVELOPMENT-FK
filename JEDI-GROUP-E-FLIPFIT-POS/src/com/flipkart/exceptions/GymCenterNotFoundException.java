package com.flipkart.exceptions;

public class GymCenterNotFoundException extends Exception {
	public String getMessage() {
        return "No gym center found!";
    }

}
