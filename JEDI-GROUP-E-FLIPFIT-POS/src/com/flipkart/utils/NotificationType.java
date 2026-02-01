package com.flipkart.utils;

/**
 * NotificationType - Types of notifications
 * 
 * From Activity Diagrams:
 * - GYM_APPROVED: Admin approved gym center
 * - GYM_REJECTED: Admin rejected gym center ("Send rejection notification")
 * - BOOKING_CONFIRMED: Slot booking confirmed ("Generate Success Notification")
 * - BOOKING_CANCELLED: Booking was cancelled
 * - WAITLIST_PROMOTION: Customer promoted from waitlist ("Promotion Notification")
 */
public enum NotificationType {
    GYM_APPROVED,
    GYM_REJECTED,
    BOOKING_CONFIRMED,
    BOOKING_CANCELLED,
    WAITLIST_PROMOTION
}
