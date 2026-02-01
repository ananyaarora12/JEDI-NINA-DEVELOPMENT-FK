package com.flipkart.bean;

import java.sql.Timestamp;

/**
 * Notification Bean - Represents a notification sent to a user
 * 
 * From Activity Diagrams:
 * - "Send rejection notification" (Admin rejects gym)
 * - "Promotion Notification" (User promoted from waitlist)
 * - "Generate Success Notification" (Booking confirmed)
 */
public class Notification {
    
    private Long notificationId;
    private Long userId;            // Who receives the notification
    private String message;
    private String type;            // GYM_APPROVED, GYM_REJECTED, BOOKING_CONFIRMED, WAITLIST_PROMOTION
    private boolean isRead;
    private Timestamp createdAt;

    // ==================== GETTERS & SETTERS ====================

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
