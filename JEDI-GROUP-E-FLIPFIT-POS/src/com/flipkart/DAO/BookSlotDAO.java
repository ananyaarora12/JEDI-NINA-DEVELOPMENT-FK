package com.flipkart.DAO;

import com.flipkart.bean.BookSlot;
import com.flipkart.constants.Constants;
import com.flipkart.utils.BookingStatusType;
import com.flipkart.utils.DB_utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * BookSlotDAO - Implementation of BookSlotDAOInterface
 * Activity: "Create Booking Record", "View My Plan", "Delete Booking"
 */
public class BookSlotDAO implements BookSlotDAOInterface {

    @Override
    public void addBooking(BookSlot booking) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.ADD_BOOKING);
            stmt.setLong(1, booking.getCustomerId());
            stmt.setLong(2, booking.getSlotId());
            stmt.setDate(3, booking.getSlotDate());
            stmt.setString(4, booking.getBookingStatus().toString());
            stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Error adding booking: " + e.getMessage());
        }
    }

    @Override
    public BookSlot getBookingById(Long bookingId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_BOOKING_BY_ID);
            stmt.setLong(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<BookSlot> getBookingsByCustomer(Long customerId) {
        List<BookSlot> bookings = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_BOOKINGS_BY_CUSTOMER);
            stmt.setLong(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return bookings;
    }

    @Override
    public List<BookSlot> getBookingsByCustomerAndDate(Long customerId, Date slotDate) {
        List<BookSlot> bookings = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_BOOKINGS_BY_CUSTOMER_AND_DATE);
            stmt.setLong(1, customerId);
            stmt.setDate(2, slotDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return bookings;
    }

    @Override
    public BookSlot checkBookingConflict(Long customerId, Long slotId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.CHECK_BOOKING_CONFLICT);
            stmt.setLong(1, customerId);
            stmt.setLong(2, slotId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public BookSlot checkBookingConflictByTime(Long customerId, Date slotDate, String startTime, String endTime) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.CHECK_BOOKING_CONFLICT_BY_TIME);
            stmt.setLong(1, customerId);
            stmt.setDate(2, slotDate);
            stmt.setString(3, startTime);
            stmt.setString(4, endTime);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void updateBookingStatus(Long bookingId, String status) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.UPDATE_BOOKING_STATUS);
            stmt.setString(1, status);
            stmt.setLong(2, bookingId);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void cancelBooking(Long bookingId) {
        updateBookingStatus(bookingId, BookingStatusType.CANCELLED.toString());
    }

    @Override
    public void deleteBooking(Long bookingId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.DELETE_BOOKING);
            stmt.setLong(1, bookingId);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private BookSlot mapResultSet(ResultSet rs) throws Exception {
        BookSlot booking = new BookSlot();
        booking.setBookingId(rs.getLong("booking_id"));
        booking.setCustomerId(rs.getLong("customer_id"));
        booking.setSlotId(rs.getLong("slot_id"));
        booking.setSlotDate(rs.getDate("slot_date"));
        booking.setBookingStatus(BookingStatusType.valueOf(rs.getString("status").toUpperCase()));
        booking.setBookedAt(rs.getTimestamp("booked_at"));
        return booking;
    }
}
