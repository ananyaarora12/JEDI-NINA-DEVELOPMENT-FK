package com.flipkart.DAO;

import com.flipkart.bean.Slot;
import com.flipkart.constants.Constants;
import com.flipkart.utils.DB_utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * SlotsDAO - Implementation of SlotsDAOInterface
 * Activity: "Configure Slots and Capacity", "Choose Date & Time Slot", "Decrement/Increment Seat Count"
 */
public class SlotsDAO implements SlotsDAOInterface {

    @Override
    public void addSlot(Slot slot) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.ADD_SLOT);
            stmt.setLong(1, slot.getCentreId());
            stmt.setDate(2, slot.getDate());
            stmt.setString(3, slot.getStartTime());
            stmt.setString(4, slot.getEndTime());
            stmt.setInt(5, slot.getTotalSeats());
            stmt.setInt(6, slot.getAvailableSeats());
            stmt.setInt(7, slot.getPrice());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Error adding slot: " + e.getMessage());
        }
    }

    @Override
    public Slot getSlotById(Long slotId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_SLOT_BY_ID);
            stmt.setLong(1, slotId);
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
    public List<Slot> getAllSlotsByGymCenterId(Long gymCenterId) {
        List<Slot> slots = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_ALL_SLOTS_BY_CENTER);
            stmt.setLong(1, gymCenterId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                slots.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return slots;
    }

    @Override
    public List<Slot> getSlotsByCenterAndDate(Long centerId, Date date) {
        List<Slot> slots = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_SLOTS_BY_CENTER_AND_DATE);
            stmt.setLong(1, centerId);
            stmt.setDate(2, date);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                slots.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return slots;
    }

    @Override
    public List<Slot> getAvailableSlotsByCenter(Long centerId) {
        List<Slot> slots = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_AVAILABLE_SLOTS_BY_CENTER);
            stmt.setLong(1, centerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                slots.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return slots;
    }

    @Override
    public List<Slot> getBookedSlotsByCustomerAndDate(Long customerId, Date date) {
        List<Slot> slots = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_BOOKED_SLOTS_BY_CUSTOMER_AND_DATE);
            stmt.setLong(1, customerId);
            stmt.setDate(2, date);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                slots.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return slots;
    }

    @Override
    public boolean decrementSeatCount(Long slotId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.DECREMENT_SEAT_COUNT);
            stmt.setLong(1, slotId);
            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean incrementSeatCount(Long slotId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.INCREMENT_SEAT_COUNT);
            stmt.setLong(1, slotId);
            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void deleteSlot(Long slotId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.DELETE_SLOT);
            stmt.setLong(1, slotId);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Slot mapResultSet(ResultSet rs) throws Exception {
        Slot slot = new Slot();
        slot.setSlotID(rs.getLong("slot_id"));
        slot.setCentreId(rs.getLong("center_id"));
        slot.setDate(rs.getDate("date"));
        slot.setStartTime(rs.getString("start_time"));
        slot.setEndTime(rs.getString("end_time"));
        slot.setTotalSeats(rs.getInt("total_seats"));
        slot.setAvailableSeats(rs.getInt("available_seats"));
        slot.setPrice(rs.getInt("price"));
        return slot;
    }
}
