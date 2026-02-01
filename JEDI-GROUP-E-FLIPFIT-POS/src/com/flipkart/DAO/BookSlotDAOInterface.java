package com.flipkart.DAO;

import com.flipkart.bean.BookSlot;
import java.sql.Date;
import java.util.List;

/**
 * BookSlotDAOInterface - Booking data access operations
 * Activity: "Create Booking Record", "View My Plan", "Delete Booking", "Change status"
 */
public interface BookSlotDAOInterface {
    void addBooking(BookSlot booking);
    BookSlot getBookingById(Long bookingId);
    List<BookSlot> getBookingsByCustomer(Long customerId);
    List<BookSlot> getBookingsByCustomerAndDate(Long customerId, Date slotDate);
    BookSlot checkBookingConflict(Long customerId, Long slotId);
    BookSlot checkBookingConflictByTime(Long customerId, Date slotDate, String startTime, String endTime);
    void updateBookingStatus(Long bookingId, String status);
    void cancelBooking(Long bookingId);
    void deleteBooking(Long bookingId);
}
