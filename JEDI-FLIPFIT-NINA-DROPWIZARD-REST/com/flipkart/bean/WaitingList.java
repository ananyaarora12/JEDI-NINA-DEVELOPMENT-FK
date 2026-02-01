package com.flipkart.bean;

import java.sql.Timestamp;

/**
 * WaitingList Bean - Represents a customer waiting for a slot
 * 
 * From Activity Diagram (Customer):
 * - "Add User to Waitlist" when no seats available
 * - "Check Waitlist for Slot" when booking cancelled
 * - "Select first user in queue (FIFO)"
 * - "Change status from Waitlisted to Confirmed"
 * - "Promotion Notification"
 */
public class WaitingList {
    
    private Long waitlistId;
    private Long customerId;
    private Long slotId;
    private int position;       // FIFO position (1 = first in queue)
    private Timestamp addedAt;

    // ==================== GETTERS & SETTERS ====================

    public Long getWaitlistId() {
        return waitlistId;
    }

    public void setWaitlistId(Long waitlistId) {
        this.waitlistId = waitlistId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Timestamp getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Timestamp addedAt) {
        this.addedAt = addedAt;
    }
}
