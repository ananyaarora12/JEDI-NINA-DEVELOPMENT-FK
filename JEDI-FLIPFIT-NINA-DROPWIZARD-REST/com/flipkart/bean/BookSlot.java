package com.flipkart.bean;

import com.flipkart.utils.BookingStatusType;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * BookSlot Bean - Represents a booking made by a customer
 * 
 * From Activity Diagram (Customer):
 * - "Create Booking Record & Decrement Seat Count"
 * - "Delete Booking & Increment Seat Count" on cancellation
 * - "Change status from Waitlisted to Confirmed" on promotion
 */
public class BookSlot {
    
    private Long bookingId;
    private Long customerId;
    private Long slotId;
    private Date slotDate;              // Date of the slot
    private BookingStatusType bookingStatus;  // CONFIRMED, CANCELLED, WAITLISTED
    private Timestamp bookedAt;         // When booking was made

    // ==================== GETTERS & SETTERS ====================

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
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

    public Date getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(Date slotDate) {
        this.slotDate = slotDate;
    }

    public BookingStatusType getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatusType bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Timestamp getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(Timestamp bookedAt) {
        this.bookedAt = bookedAt;
    }
    
    // Backward compatibility
    public Date getSlotBookingDate() { return this.slotDate; }
    public void setSlotBookingDate(Date date) { this.slotDate = date; }
    public Date getBookingDate() { return this.slotDate; }
    public void setBookingDate(Date date) { this.slotDate = date; }
}
