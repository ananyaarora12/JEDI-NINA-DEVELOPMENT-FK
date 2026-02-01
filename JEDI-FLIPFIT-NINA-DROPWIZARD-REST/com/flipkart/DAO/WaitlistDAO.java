package com.flipkart.DAO;

import com.flipkart.bean.WaitingList;
import com.flipkart.constants.Constants;
import com.flipkart.utils.DB_utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * WaitlistDAO - Implementation of WaitlistDAOInterface
 * Activity: "Add User to Waitlist", "Check Waitlist for Slot", "Select first user in queue (FIFO)"
 */
public class WaitlistDAO implements WaitlistDAOInterface {

    @Override
    public void addToWaitlist(WaitingList entry) {
        try {
            int position = getWaitlistCount(entry.getSlotId()) + 1;
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.ADD_TO_WAITLIST);
            stmt.setLong(1, entry.getCustomerId());
            stmt.setLong(2, entry.getSlotId());
            stmt.setInt(3, position);
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Error adding to waitlist: " + e.getMessage());
        }
    }

    @Override
    public WaitingList getFirstInWaitlist(Long slotId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_FIRST_IN_WAITLIST);
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
    public List<WaitingList> getWaitlistBySlot(Long slotId) {
        List<WaitingList> waitlist = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_WAITLIST_BY_SLOT);
            stmt.setLong(1, slotId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                waitlist.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return waitlist;
    }

    @Override
    public int getWaitlistCount(Long slotId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_WAITLIST_COUNT);
            stmt.setLong(1, slotId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public void removeFromWaitlist(Long waitlistId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.REMOVE_FROM_WAITLIST);
            stmt.setLong(1, waitlistId);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public boolean isCustomerInWaitlist(Long customerId, Long slotId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.CHECK_CUSTOMER_IN_WAITLIST);
            stmt.setLong(1, customerId);
            stmt.setLong(2, slotId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public int getCustomerPosition(Long customerId, Long slotId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.CHECK_CUSTOMER_IN_WAITLIST);
            stmt.setLong(1, customerId);
            stmt.setLong(2, slotId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("position");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return -1;
    }

    private WaitingList mapResultSet(ResultSet rs) throws Exception {
        WaitingList entry = new WaitingList();
        entry.setWaitlistId(rs.getLong("waitlist_id"));
        entry.setCustomerId(rs.getLong("customer_id"));
        entry.setSlotId(rs.getLong("slot_id"));
        entry.setPosition(rs.getInt("position"));
        entry.setAddedAt(rs.getTimestamp("added_at"));
        return entry;
    }
}
