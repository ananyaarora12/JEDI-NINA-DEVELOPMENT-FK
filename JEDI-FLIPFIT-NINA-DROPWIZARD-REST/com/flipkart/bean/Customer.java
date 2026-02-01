package com.flipkart.bean;

/**
 * Customer Bean - Represents a gym customer
 * 
 * From Activity Diagram (Customer):
 * - "Sign up" if not registered
 * - "Login" to authenticate
 * - "Select City" to filter gym centers
 * - "View My Plan" to see bookings
 */
public class Customer {
    
    private Long customerId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String city;      // For "Select City" in activity diagram
    private String address;

    // ==================== GETTERS & SETTERS ====================

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    // Backward compatibility methods
    public String getCustomerName() { return this.name; }
    public void setCustomerName(String name) { this.name = name; }
    public String getCustomerEmailAddress() { return this.email; }
    public void setCustomerEmailAddress(String email) { this.email = email; }
    public String getCustomerPhone() { return this.phone; }
    public void setCustomerPhone(String phone) { this.phone = phone; }
    public String getCustomerAddress() { return this.address; }
    public void setCustomerAddress(String address) { this.address = address; }
}
