package com.flipkart.exceptions;

/**
 * CustomExceptions - Collection of custom exceptions for FlipFit
 * Based on Activity Diagram decision points
 */
public class CustomExceptions {

    /**
     * Thrown when authentication fails
     * Activity: "Authenticate Credentials" -> [Failed]
     */
    public static class AuthenticationFailedException extends Exception {
        public AuthenticationFailedException() {
            super("Authentication failed. Invalid email or password.");
        }
        public AuthenticationFailedException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when no seats are available
     * Activity: "Seats Available?" -> [NO]
     */
    public static class NoSeatsAvailableException extends Exception {
        public NoSeatsAvailableException(Long slotId) {
            super("No seats available for slot ID: " + slotId);
        }
    }

    /**
     * Thrown when booking conflict detected
     * Activity: "Conflict Found?" -> [YES]
     */
    public static class BookingConflictException extends Exception {
        public BookingConflictException() {
            super("Booking conflict detected. You already have a booking for this time.");
        }
    }

    /**
     * Thrown when gym center not approved
     * Activity: "Approve?" -> [NO]
     */
    public static class GymNotApprovedException extends Exception {
        public GymNotApprovedException() {
            super("Gym center is not approved yet.");
        }
    }

    /**
     * Thrown when input validation fails
     * Activity: "Input Valid?" -> [NO]
     */
    public static class InvalidInputException extends Exception {
        public InvalidInputException(String message) {
            super(message);
        }
    }
}
