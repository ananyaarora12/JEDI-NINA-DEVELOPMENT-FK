package com.flipkart.utils;

/**
 * BookingStatusType - Status of a slot booking
 * 
 * From Activity Diagram:
 * - CONFIRMED: Booking successful
 * - CANCELLED: Booking cancelled
 * - WAITLISTED: Added to waitlist (no seats)
 */
public enum BookingStatusType {
    CONFIRMED,
    CANCELLED,
    WAITLISTED
}
