package com.flipkart.bean;

import java.sql.Date;

/**
 * Slot Bean - Represents a time slot at a gym center
 * 
 * Maps to: slot table in database
 * 
 * Relationships:
 * - Belongs to one GymCenter (centreId)
 * - Has many Bookings
 * - Has one WaitingList
 * 
 * From Activity Diagram:
 * - Customer "Choose Date & Time Slot"
 * - System "Check Slot Seat Count"
 * - System "Verify Seat Availability"
 * - GymOwner "Configure Slots and Capacity"
 */
public class Slot {
    
    // Primary Key
    private Long slotID;
    
    // Foreign Key - belongs to GymCenter
    private Long centreId;
    
    // Date (from Activity Diagram: "Choose Date & Time Slot")
    private Date date;              // NEW: The date of the slot
    
    // Time (from Activity Diagram: "Choose Date & Time Slot")
    private String startTime;       // 24hr format: "09:00"
    private String endTime;         // 24hr format: "10:00"
    
    // Capacity (from Activity Diagram: "Check Slot Seat Count")
    private int totalSeats;         // NEW: Total seats for this slot
    private int availableSeats;     // Seats still available
    
    // Pricing
    private int price;
    
    // Waiting list reference (in-memory, not stored in DB)
    private WaitingList waitList;

    // ==================== GETTERS & SETTERS ====================

    public Long getSlotID() {
        return slotID;
    }

    public void setSlotID(Long slotID) {
        this.slotID = slotID;
    }

    public Long getCentreId() {
        return centreId;
    }

    public void setCentreId(Long centreId) {
        this.centreId = centreId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
    
    public void setAvailableSeats(long availableSeats) {
        this.availableSeats = (int) availableSeats;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public WaitingList getWaitList() {
        return waitList;
    }

    public void setWaitList(WaitingList waitList) {
        this.waitList = waitList;
    }
    
    // Helper method: Check if seats are available
    public boolean hasAvailableSeats() {
        return this.availableSeats > 0;
    }
    
    // Helper method: Get slot time display
    public String getSlotTimings() {
        if (this.startTime != null && this.endTime != null) {
            return this.startTime + " - " + this.endTime;
        }
        return null;
    }
    
    // Backward compatibility: Set timings from "HH:MM - HH:MM" or "HH:MM" format
    public void setSlotTimings(String timings) {
        if (timings != null && timings.contains("-")) {
            String[] parts = timings.split("-");
            this.startTime = parts[0].trim();
            this.endTime = parts.length > 1 ? parts[1].trim() : parts[0].trim();
        } else {
            this.startTime = timings;
            this.endTime = timings;
        }
    }
}
