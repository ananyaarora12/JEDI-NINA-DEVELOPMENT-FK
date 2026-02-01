package com.flipkart.DAO;

import com.flipkart.bean.GymCenter;
import com.flipkart.constants.Constants;
import com.flipkart.utils.DB_utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * GymCenterDAO - Implementation of GymCenterDAOInterface
 * Activity: "Add a new centre", "View Pending Centers", "Approve?", "Select City"
 */
public class GymCenterDAO implements GymCenterDAOInterface {

    @Override
    public void addGymCenter(GymCenter gymCenter) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.ADD_GYM_CENTER);
            stmt.setString(1, gymCenter.getName());
            stmt.setString(2, gymCenter.getCity());
            stmt.setString(3, gymCenter.getLocation());
            stmt.setInt(4, gymCenter.getCapacity());
            stmt.setLong(5, gymCenter.getGymOwnerId());
            stmt.executeUpdate();
            stmt.close();
            System.out.println("Gym center added successfully");
        } catch (Exception e) {
            System.out.println("Error adding gym center: " + e.getMessage());
        }
    }

    @Override
    public GymCenter getGymCenterById(Long centerId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_GYM_CENTER_BY_ID);
            stmt.setLong(1, centerId);
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
    public List<GymCenter> getAllApprovedGymCenters() {
        List<GymCenter> gymCenters = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_ALL_APPROVED_GYM_CENTERS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                gymCenters.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return gymCenters;
    }

    @Override
    public List<GymCenter> getApprovedGymCentersByCity(String city) {
        List<GymCenter> gymCenters = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_APPROVED_GYM_CENTERS_BY_CITY);
            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                gymCenters.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return gymCenters;
    }

    @Override
    public List<GymCenter> getPendingGymCenters() {
        List<GymCenter> gymCenters = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_PENDING_GYM_CENTERS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                gymCenters.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return gymCenters;
    }

    @Override
    public List<GymCenter> getGymCentersByOwner(Long ownerId) {
        List<GymCenter> gymCenters = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_GYM_CENTERS_BY_OWNER);
            stmt.setLong(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                gymCenters.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return gymCenters;
    }

    @Override
    public void approveGymCenter(Long centerId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.APPROVE_GYM_CENTER);
            stmt.setLong(1, centerId);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void rejectGymCenter(Long centerId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.REJECT_GYM_CENTER);
            stmt.setLong(1, centerId);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Legacy method for backward compatibility
    public List<GymCenter> getAllGymCenters() {
        List<GymCenter> gymCenters = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM gym_center");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                gymCenters.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return gymCenters;
    }

    private GymCenter mapResultSet(ResultSet rs) throws Exception {
        GymCenter gymCenter = new GymCenter();
        gymCenter.setId(rs.getLong("center_id"));
        gymCenter.setName(rs.getString("name"));
        gymCenter.setCity(rs.getString("city"));
        gymCenter.setLocation(rs.getString("location"));
        gymCenter.setCapacity(rs.getInt("capacity"));
        gymCenter.setStatus(rs.getString("status"));
        gymCenter.setGymOwnerId(rs.getLong("owner_id"));
        return gymCenter;
    }
}
