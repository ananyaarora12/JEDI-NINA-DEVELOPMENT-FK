-- ============================================
-- FlipFit Database Schema
-- Based on Activity Diagrams: Admin, GymOwner, Customer
-- ============================================

-- Create database
CREATE DATABASE IF NOT EXISTS flipfit1;
USE flipfit1;

-- ============================================
-- TABLE 1: CUSTOMER
-- Activity: "Sign up", "Login", "Select City"
-- ============================================
CREATE TABLE customer (
    customer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15),
    password VARCHAR(255) NOT NULL,
    city VARCHAR(50),
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TABLE 2: GYM_OWNER
-- Activity: "Sign up", "Login", "Add a new centre"
-- ============================================
CREATE TABLE gym_owner (
    owner_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15),
    pan_number VARCHAR(20),
    address VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    is_approved TINYINT DEFAULT 0,  -- 0 = PENDING, 1 = APPROVED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TABLE 3: GYM_ADMIN
-- Activity: "Log in to FlipFit Admin", "Authenticate Credentials"
-- ============================================
CREATE TABLE gym_admin (
    admin_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15),
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TABLE 4: GYM_CENTER
-- Activity: "Add a new centre (Location, Name, Capacity)"
--           "View Pending Centers", "Approve?", "Select City"
-- ============================================
CREATE TABLE gym_center (
    center_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    location VARCHAR(255),
    capacity INT DEFAULT 0,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES gym_owner(owner_id) ON DELETE CASCADE
);

-- ============================================
-- TABLE 5: SLOT
-- Activity: "Configure Slots and Capacity", "Choose Date & Time Slot"
--           "Check Slot Seat Count", "Decrement/Increment Seat Count"
-- ============================================
CREATE TABLE slot (
    slot_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    center_id BIGINT NOT NULL,
    date DATE NOT NULL,
    start_time VARCHAR(10) NOT NULL,  -- Format: "09:00"
    end_time VARCHAR(10) NOT NULL,    -- Format: "10:00"
    total_seats INT NOT NULL,
    available_seats INT NOT NULL,
    price INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (center_id) REFERENCES gym_center(center_id) ON DELETE CASCADE
);

-- ============================================
-- TABLE 6: BOOKING
-- Activity: "Create Booking Record & Decrement Seat Count"
--           "View My Plan", "Delete Booking & Increment Seat Count"
--           "Change status from Waitlisted to Confirmed"
-- ============================================
CREATE TABLE booking (
    booking_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    slot_id BIGINT NOT NULL,
    slot_date DATE NOT NULL,
    status ENUM('CONFIRMED', 'CANCELLED', 'WAITLISTED') DEFAULT 'CONFIRMED',
    booked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (slot_id) REFERENCES slot(slot_id) ON DELETE CASCADE
);

-- ============================================
-- TABLE 7: WAITLIST
-- Activity: "Add User to Waitlist", "Check Waitlist for Slot"
--           "Select first user in queue (FIFO)", "Promotion Notification"
-- ============================================
CREATE TABLE waitlist (
    waitlist_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    slot_id BIGINT NOT NULL,
    position INT NOT NULL,  -- FIFO order (1 = first in queue)
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (slot_id) REFERENCES slot(slot_id) ON DELETE CASCADE
);

-- ============================================
-- TABLE 8: NOTIFICATION
-- Activity: "Send rejection notification", "Promotion Notification"
--           "Generate Success Notification"
-- ============================================
CREATE TABLE notification (
    notification_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    type ENUM('GYM_APPROVED', 'GYM_REJECTED', 'BOOKING_CONFIRMED', 'BOOKING_CANCELLED', 'WAITLIST_PROMOTION') NOT NULL,
    is_read TINYINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TABLE 9: USER_ROLE (for authentication routing)
-- ============================================
CREATE TABLE user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    user_role ENUM('ADMIN', 'CUSTOMER', 'OWNER') NOT NULL,
    user_email VARCHAR(100) NOT NULL
);

-- ============================================
-- INDEXES for better query performance
-- ============================================
CREATE INDEX idx_customer_email ON customer(email);
CREATE INDEX idx_customer_city ON customer(city);
CREATE INDEX idx_gym_owner_email ON gym_owner(email);
CREATE INDEX idx_gym_admin_email ON gym_admin(email);
CREATE INDEX idx_gym_center_city ON gym_center(city);
CREATE INDEX idx_gym_center_status ON gym_center(status);
CREATE INDEX idx_gym_center_owner ON gym_center(owner_id);
CREATE INDEX idx_slot_center ON slot(center_id);
CREATE INDEX idx_slot_date ON slot(date);
CREATE INDEX idx_booking_customer ON booking(customer_id);
CREATE INDEX idx_booking_slot ON booking(slot_id);
CREATE INDEX idx_booking_status ON booking(status);
CREATE INDEX idx_waitlist_slot ON waitlist(slot_id);
CREATE INDEX idx_waitlist_position ON waitlist(position);
CREATE INDEX idx_notification_user ON notification(user_id);
CREATE INDEX idx_user_role_email ON user_role(user_email);

-- ============================================
-- INSERT DEFAULT ADMIN
-- ============================================
INSERT INTO gym_admin (name, email, phone, password) 
VALUES ('Super Admin', 'admin@flipfit.com', '9999999999', 'admin123');

-- ============================================
-- SAMPLE DATA FOR TESTING
-- ============================================

-- Sample Gym Owner (Pending approval)
INSERT INTO gym_owner (name, email, phone, pan_number, address, password, is_approved) 
VALUES ('John Owner', 'owner@test.com', '9876543210', 'ABCDE1234F', 'Mumbai', 'owner123', 0);

-- Sample Gym Owner (Approved)
INSERT INTO gym_owner (name, email, phone, pan_number, address, password, is_approved) 
VALUES ('Jane Owner', 'jane@test.com', '9876543211', 'FGHIJ5678K', 'Delhi', 'owner123', 1);

-- Sample Gym Center (Approved)
INSERT INTO gym_center (name, city, location, capacity, status, owner_id) 
VALUES ('FitZone Mumbai', 'Mumbai', '123 Main Street, Andheri', 50, 'APPROVED', 2);

-- Sample Gym Center (Pending)
INSERT INTO gym_center (name, city, location, capacity, status, owner_id) 
VALUES ('PowerGym Delhi', 'Delhi', '456 Park Road, Connaught Place', 30, 'PENDING', 1);

-- Sample Slots for FitZone Mumbai
INSERT INTO slot (center_id, date, start_time, end_time, total_seats, available_seats, price) 
VALUES 
(1, CURDATE(), '06:00', '07:00', 10, 10, 500),
(1, CURDATE(), '07:00', '08:00', 10, 8, 500),
(1, CURDATE(), '08:00', '09:00', 10, 5, 500),
(1, CURDATE(), '17:00', '18:00', 10, 10, 600),
(1, CURDATE(), '18:00', '19:00', 10, 3, 600),
(1, CURDATE() + INTERVAL 1 DAY, '06:00', '07:00', 10, 10, 500),
(1, CURDATE() + INTERVAL 1 DAY, '07:00', '08:00', 10, 10, 500);

-- Sample Customer
INSERT INTO customer (name, email, phone, password, city, address) 
VALUES ('Test Customer', 'customer@test.com', '9123456789', 'customer123', 'Mumbai', 'Mumbai, Maharashtra');

-- Add user roles
INSERT INTO user_role (user_id, user_role, user_email) VALUES (1, 'ADMIN', 'admin@flipfit.com');
INSERT INTO user_role (user_id, user_role, user_email) VALUES (1, 'CUSTOMER', 'customer@test.com');
INSERT INTO user_role (user_id, user_role, user_email) VALUES (1, 'OWNER', 'owner@test.com');
INSERT INTO user_role (user_id, user_role, user_email) VALUES (2, 'OWNER', 'jane@test.com');

-- ============================================
-- VIEWS for common queries
-- ============================================

-- View: Approved Gym Centers with Owner Info
CREATE OR REPLACE VIEW v_approved_gyms AS
SELECT 
    gc.center_id,
    gc.name AS gym_name,
    gc.city,
    gc.location,
    gc.capacity,
    go.name AS owner_name,
    go.email AS owner_email
FROM gym_center gc
JOIN gym_owner go ON gc.owner_id = go.owner_id
WHERE gc.status = 'APPROVED';

-- View: Available Slots (seats > 0)
CREATE OR REPLACE VIEW v_available_slots AS
SELECT 
    s.slot_id,
    s.date,
    s.start_time,
    s.end_time,
    s.available_seats,
    s.price,
    gc.name AS gym_name,
    gc.city
FROM slot s
JOIN gym_center gc ON s.center_id = gc.center_id
WHERE s.available_seats > 0 AND gc.status = 'APPROVED';

-- View: Customer Bookings
CREATE OR REPLACE VIEW v_customer_bookings AS
SELECT 
    b.booking_id,
    b.slot_date,
    b.status,
    b.booked_at,
    c.name AS customer_name,
    c.email AS customer_email,
    s.start_time,
    s.end_time,
    gc.name AS gym_name,
    gc.city
FROM booking b
JOIN customer c ON b.customer_id = c.customer_id
JOIN slot s ON b.slot_id = s.slot_id
JOIN gym_center gc ON s.center_id = gc.center_id;

-- ============================================
-- END OF SCHEMA
-- ============================================
