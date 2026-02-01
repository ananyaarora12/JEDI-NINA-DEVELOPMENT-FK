package com.flipkart.constants;

/**
 * Constants - SQL Queries organized by entity
 * Based on Activity Diagrams: Admin, GymOwner, Customer
 */
public class Constants {

    // ==================== CUSTOMER QUERIES ====================
    // Activity: "Sign up", "Login", "Select City"
    
    public static final String ADD_CUSTOMER = 
        "INSERT INTO customer (name, email, phone, password, city, address) VALUES (?, ?, ?, ?, ?, ?)";
    
    public static final String GET_CUSTOMER_BY_EMAIL = 
        "SELECT * FROM customer WHERE email = ?";
    
    public static final String GET_CUSTOMER_BY_ID = 
        "SELECT * FROM customer WHERE customer_id = ?";
    
    public static final String AUTHENTICATE_CUSTOMER = 
        "SELECT * FROM customer WHERE email = ? AND password = ?";

    // ==================== GYM OWNER QUERIES ====================
    // Activity: "Sign up", "Login", "Add a new centre"
    
    public static final String ADD_GYM_OWNER = 
        "INSERT INTO gym_owner (name, email, phone, pan_number, address, password, is_approved) VALUES (?, ?, ?, ?, ?, ?, 0)";
    
    public static final String GET_GYM_OWNER_BY_EMAIL = 
        "SELECT * FROM gym_owner WHERE email = ?";
    
    public static final String GET_GYM_OWNER_BY_ID = 
        "SELECT * FROM gym_owner WHERE owner_id = ?";
    
    public static final String AUTHENTICATE_GYM_OWNER = 
        "SELECT * FROM gym_owner WHERE email = ? AND password = ?";
    
    public static final String GET_PENDING_GYM_OWNERS = 
        "SELECT * FROM gym_owner WHERE is_approved = 0";
    
    public static final String GET_APPROVED_GYM_OWNERS = 
        "SELECT * FROM gym_owner WHERE is_approved = 1";
    
    public static final String APPROVE_GYM_OWNER = 
        "UPDATE gym_owner SET is_approved = 1 WHERE owner_id = ?";
    
    public static final String REJECT_GYM_OWNER = 
        "DELETE FROM gym_owner WHERE owner_id = ?";

    // ==================== ADMIN QUERIES ====================
    // Activity: "Log in to FlipFit Admin", "Authenticate Credentials"
    
    public static final String ADD_ADMIN = 
        "INSERT INTO gym_admin (name, email, phone, password) VALUES (?, ?, ?, ?)";
    
    public static final String GET_ADMIN_BY_EMAIL = 
        "SELECT * FROM gym_admin WHERE email = ?";
    
    public static final String AUTHENTICATE_ADMIN = 
        "SELECT * FROM gym_admin WHERE email = ? AND password = ?";

    // ==================== GYM CENTER QUERIES ====================
    // Activity: "Add a new centre", "View Pending Centers", "Approve?"
    
    public static final String ADD_GYM_CENTER = 
        "INSERT INTO gym_center (name, city, location, capacity, status, owner_id) VALUES (?, ?, ?, ?, 'PENDING', ?)";
    
    public static final String GET_GYM_CENTER_BY_ID = 
        "SELECT * FROM gym_center WHERE center_id = ?";
    
    // Activity: "Select City" -> "Fetch Active Centers List"
    public static final String GET_APPROVED_GYM_CENTERS_BY_CITY = 
        "SELECT * FROM gym_center WHERE city = ? AND status = 'APPROVED'";
    
    public static final String GET_ALL_APPROVED_GYM_CENTERS = 
        "SELECT * FROM gym_center WHERE status = 'APPROVED'";
    
    // Activity: "View Pending Centers"
    public static final String GET_PENDING_GYM_CENTERS = 
        "SELECT * FROM gym_center WHERE status = 'PENDING'";
    
    // Activity: "Approve?" -> "Save Center & Slot Info"
    public static final String APPROVE_GYM_CENTER = 
        "UPDATE gym_center SET status = 'APPROVED' WHERE center_id = ?";
    
    // Activity: "Approve?" [no] -> "Delete Centre"
    public static final String REJECT_GYM_CENTER = 
        "UPDATE gym_center SET status = 'REJECTED' WHERE center_id = ?";
    
    public static final String DELETE_GYM_CENTER = 
        "DELETE FROM gym_center WHERE center_id = ?";
    
    public static final String GET_GYM_CENTERS_BY_OWNER = 
        "SELECT * FROM gym_center WHERE owner_id = ?";

    // ==================== SLOT QUERIES ====================
    // Activity: "Configure Slots and Capacity", "Choose Date & Time Slot"
    
    public static final String ADD_SLOT = 
        "INSERT INTO slot (center_id, date, start_time, end_time, total_seats, available_seats, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    public static final String GET_SLOT_BY_ID = 
        "SELECT * FROM slot WHERE slot_id = ?";
    
    // Activity: "Choose Date & Time Slot"
    public static final String GET_SLOTS_BY_CENTER_AND_DATE = 
        "SELECT * FROM slot WHERE center_id = ? AND date = ?";
    
    public static final String GET_AVAILABLE_SLOTS_BY_CENTER = 
        "SELECT * FROM slot WHERE center_id = ? AND available_seats > 0";
    
    public static final String GET_ALL_SLOTS_BY_CENTER = 
        "SELECT * FROM slot WHERE center_id = ?";

    // Activity: "Check Existing Bookings for Date"
    public static final String GET_BOOKED_SLOTS_BY_CUSTOMER_AND_DATE = 
        "SELECT s.* FROM slot s JOIN booking b ON b.slot_id = s.slot_id WHERE b.customer_id = ? AND b.status = 'CONFIRMED' AND b.slot_date = ?";
    
    // Activity: "Decrement Seat Count", "Increment Seat Count"
    public static final String DECREMENT_SEAT_COUNT = 
        "UPDATE slot SET available_seats = available_seats - 1 WHERE slot_id = ? AND available_seats > 0";
    
    public static final String INCREMENT_SEAT_COUNT = 
        "UPDATE slot SET available_seats = available_seats + 1 WHERE slot_id = ?";
    
    public static final String DELETE_SLOT = 
        "DELETE FROM slot WHERE slot_id = ?";

    // ==================== BOOKING QUERIES ====================
    // Activity: "Create Booking Record", "Delete Booking", "View My Plan"
    
    public static final String ADD_BOOKING = 
        "INSERT INTO booking (customer_id, slot_id, slot_date, status, booked_at) VALUES (?, ?, ?, ?, ?)";
    
    public static final String GET_BOOKING_BY_ID = 
        "SELECT * FROM booking WHERE booking_id = ?";
    
    // Activity: "View My Plan"
    public static final String GET_BOOKINGS_BY_CUSTOMER = 
        "SELECT * FROM booking WHERE customer_id = ? ORDER BY slot_date DESC";

    public static final String GET_BOOKINGS_BY_CUSTOMER_AND_DATE = 
        "SELECT * FROM booking WHERE customer_id = ? AND slot_date = ? ORDER BY slot_date DESC";
    
    // Activity: "Initiate Conflict Check"
    public static final String CHECK_BOOKING_CONFLICT = 
        "SELECT * FROM booking WHERE customer_id = ? AND slot_id = ? AND status = 'CONFIRMED'";

    public static final String CHECK_BOOKING_CONFLICT_BY_TIME = 
        "SELECT b.* FROM booking b JOIN slot s ON b.slot_id = s.slot_id WHERE b.customer_id = ? AND b.status = 'CONFIRMED' AND b.slot_date = ? AND s.start_time = ? AND s.end_time = ? LIMIT 1";
    
    // Activity: "Change status from Waitlisted to Confirmed"
    public static final String UPDATE_BOOKING_STATUS = 
        "UPDATE booking SET status = ? WHERE booking_id = ?";
    
    public static final String CANCEL_BOOKING = 
        "UPDATE booking SET status = 'CANCELLED' WHERE booking_id = ?";
    
    public static final String DELETE_BOOKING = 
        "DELETE FROM booking WHERE booking_id = ?";

    // ==================== WAITLIST QUERIES ====================
    // Activity: "Add User to Waitlist", "Check Waitlist for Slot", "Select first user in queue (FIFO)"
    
    public static final String ADD_TO_WAITLIST = 
        "INSERT INTO waitlist (customer_id, slot_id, position, added_at) VALUES (?, ?, ?, ?)";
    
    public static final String GET_WAITLIST_COUNT = 
        "SELECT COUNT(*) FROM waitlist WHERE slot_id = ?";
    
    // Activity: "Select first user in queue (FIFO)"
    public static final String GET_FIRST_IN_WAITLIST = 
        "SELECT * FROM waitlist WHERE slot_id = ? ORDER BY position ASC LIMIT 1";
    
    public static final String GET_WAITLIST_BY_SLOT = 
        "SELECT * FROM waitlist WHERE slot_id = ? ORDER BY position ASC";
    
    public static final String REMOVE_FROM_WAITLIST = 
        "DELETE FROM waitlist WHERE waitlist_id = ?";
    
    public static final String UPDATE_WAITLIST_POSITIONS = 
        "UPDATE waitlist SET position = position - 1 WHERE slot_id = ? AND position > ?";
    
    public static final String CHECK_CUSTOMER_IN_WAITLIST = 
        "SELECT * FROM waitlist WHERE customer_id = ? AND slot_id = ?";

    // ==================== NOTIFICATION QUERIES ====================
    // Activity: "Send rejection notification", "Promotion Notification", "Generate Success Notification"
    
    public static final String ADD_NOTIFICATION = 
        "INSERT INTO notification (user_id, message, type, is_read, created_at) VALUES (?, ?, ?, 0, ?)";
    
    public static final String GET_NOTIFICATIONS_BY_USER = 
        "SELECT * FROM notification WHERE user_id = ? ORDER BY created_at DESC";
    
    public static final String GET_UNREAD_NOTIFICATIONS = 
        "SELECT * FROM notification WHERE user_id = ? AND is_read = 0";
    
    public static final String MARK_NOTIFICATION_READ = 
        "UPDATE notification SET is_read = 1 WHERE notification_id = ?";

    // ==================== USER ROLE QUERIES ====================
    
    public static final String ADD_USER_ROLE = 
        "INSERT INTO user_role (user_id, user_role, user_email) VALUES (?, ?, ?)";
    
    public static final String GET_USER_ROLE_BY_EMAIL = 
        "SELECT * FROM user_role WHERE user_email = ?";
}
