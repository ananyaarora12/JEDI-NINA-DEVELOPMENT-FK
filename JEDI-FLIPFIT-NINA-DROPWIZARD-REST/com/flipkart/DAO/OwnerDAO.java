package com.flipkart.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.flipkart.bean.GymCenter;
import com.flipkart.bean.GymOwner;
import com.flipkart.bean.Slot;
import com.flipkart.bean.UserRole;
import com.flipkart.constants.Constants;
import com.flipkart.utils.DB_utils;
import com.flipkart.utils.UserRoleType;

/**
 * OwnerDAO - Implementation of OwnerDAOInterface
 * Activity: "Sign up" (GymOwner), "Add a new centre", "Configure Slots"
 */
public class OwnerDAO implements OwnerDAOInterface {

    private UserDao userDao = new UserDao();

    @Override
    public void addOwner(GymOwner gymOwner) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.ADD_GYM_OWNER);
            stmt.setString(1, gymOwner.getOwnerName());
            stmt.setString(2, gymOwner.getOwnerEmailAddress());
            stmt.setString(3, gymOwner.getOwnerPhone());
            stmt.setString(4, gymOwner.getOwnerPanNum());
            stmt.setString(5, gymOwner.getOwnerAddress());
            stmt.setString(6, gymOwner.getPassword());
            stmt.executeUpdate();
            stmt.close();
            
            // Add user role
            GymOwner saved = getGymOwnerByEmail(gymOwner.getOwnerEmailAddress());
            if (saved != null) {
                UserRole userRole = new UserRole();
                userRole.setUserId(saved.getOwnerId());
                userRole.setUserEmail(gymOwner.getOwnerEmailAddress());
                userRole.setUserRole(UserRoleType.OWNER);
                userDao.addUserRole(userRole);
            }
            System.out.println("Owner registered successfully");
        } catch (Exception e) {
            System.out.println("Error adding owner: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<GymOwner> getAllOwners() {
        ArrayList<GymOwner> owners = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM gym_owner");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                owners.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return owners;
    }

    @Override
    public GymOwner getOwnerByID(Long gymOwnerID) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_GYM_OWNER_BY_ID);
            stmt.setLong(1, gymOwnerID);
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
    public GymOwner getGymOwnerByEmail(String email) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_GYM_OWNER_BY_EMAIL);
            stmt.setString(1, email);
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
    public void addCenter(GymCenter center) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.ADD_GYM_CENTER);
            stmt.setString(1, center.getName());
            stmt.setString(2, center.getCity());
            stmt.setString(3, center.getLocation());
            stmt.setInt(4, center.getCapacity());
            stmt.setLong(5, center.getGymOwnerId());
            stmt.executeUpdate();
            stmt.close();
            System.out.println("Center added successfully");
        } catch (Exception e) {
            System.out.println("Error adding center: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<GymCenter> getAllCenters(Long ownerID) {
        ArrayList<GymCenter> centers = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_GYM_CENTERS_BY_OWNER);
            stmt.setLong(1, ownerID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                centers.add(mapCenterResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return centers;
    }

    @Override
    public ArrayList<GymCenter> getAllApprovedCenters(Long ownerID) {
        ArrayList<GymCenter> centers = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM gym_center WHERE owner_id = ? AND status = 'APPROVED'");
            stmt.setLong(1, ownerID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                centers.add(mapCenterResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return centers;
    }

    @Override
    public void addSlot(Long ownerID, Slot slot) {
        SlotsDAOInterface slotsDAO = new SlotsDAO();
        slotsDAO.addSlot(slot);
    }

    @Override
    public ArrayList<Slot> getAllSlots(Long ownerID, Long centerID) {
        SlotsDAOInterface slotsDAO = new SlotsDAO();
        return new ArrayList<>(slotsDAO.getAllSlotsByGymCenterId(centerID));
    }

    private GymOwner mapResultSet(ResultSet rs) throws Exception {
        GymOwner owner = new GymOwner();
        owner.setOwnerId(rs.getLong("owner_id"));
        owner.setOwnerName(rs.getString("name"));
        owner.setOwnerEmailAddress(rs.getString("email"));
        owner.setOwnerPhone(rs.getString("phone"));
        owner.setOwnerPanNum(rs.getString("pan_number"));
        owner.setOwnerAddress(rs.getString("address"));
        owner.setPassword(rs.getString("password"));
        owner.setApproved(rs.getBoolean("is_approved"));
        return owner;
    }

    private GymCenter mapCenterResultSet(ResultSet rs) throws Exception {
        GymCenter center = new GymCenter();
        center.setId(rs.getLong("center_id"));
        center.setName(rs.getString("name"));
        center.setCity(rs.getString("city"));
        center.setLocation(rs.getString("location"));
        center.setCapacity(rs.getInt("capacity"));
        center.setStatus(rs.getString("status"));
        center.setGymOwnerId(rs.getLong("owner_id"));
        return center;
    }
}
