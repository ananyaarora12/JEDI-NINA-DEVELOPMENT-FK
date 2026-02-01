package com.flipkart.bean;

/**
 * GymCenter Bean - Represents a gym center in the system
 * 
 * From Activity Diagram (GymOwner):
 * - "Add a new centre (Location, Name, Capacity)"
 * - "Submit gym details"
 * 
 * From Activity Diagram (Admin):
 * - "View Pending Centers"
 * - "Approve?" -> "Save Center & Slot Info" OR "Delete Centre"
 * 
 * From Activity Diagram (Customer):
 * - "Select City" -> "Fetch Active Centers List"
 */
public class GymCenter {
    
    private Long id;
    private String name;
    private String city;        // For "Select City" filter
    private String location;    // Full address
    private int capacity;       // From "Add a new centre (Location, Name, Capacity)"
    private String status;      // PENDING, APPROVED, REJECTED
    private Long gymOwnerId;    // Foreign key to GymOwner

    // ==================== GETTERS & SETTERS ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getGymOwnerId() {
        return gymOwnerId;
    }

    public void setGymOwnerId(Long gymOwnerId) {
        this.gymOwnerId = gymOwnerId;
    }
    
    public boolean isApproved() {
        return "APPROVED".equalsIgnoreCase(this.status);
    }
}
