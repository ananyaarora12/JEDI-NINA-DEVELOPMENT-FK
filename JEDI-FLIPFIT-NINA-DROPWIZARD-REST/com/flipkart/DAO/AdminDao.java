package com.flipkart.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.flipkart.bean.GymAdmin;
import com.flipkart.bean.GymCenter;
import com.flipkart.bean.GymOwner;
import com.flipkart.bean.UserRole;
import com.flipkart.constants.Constants;
import com.flipkart.utils.DB_utils;
import com.flipkart.utils.UserRoleType;

/**
 * AdminDao - Implementation of AdminDaoInterface
 * Activity: "Log in to FlipFit Admin", "View Pending Centers", "Approve?", "Delete Centre"
 */
public class AdminDao implements AdminDaoInterface {

    private UserDaoInterface userDao = new UserDao();

    @Override
    public void addAdmin(GymAdmin gymAdmin) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.ADD_ADMIN);
            stmt.setString(1, gymAdmin.getAdminName());
            stmt.setString(2, gymAdmin.getAdminEmailAddress());
            stmt.setString(3, gymAdmin.getPhone());
            stmt.setString(4, gymAdmin.getPassword());
            stmt.executeUpdate();
            stmt.close();

            // Add to user_role table
            GymAdmin saved = getAdminByEmail(gymAdmin.getAdminEmailAddress());
            if (saved != null) {
                UserRole userRole = new UserRole();
                userRole.setUserId(saved.getAdminId());
                userRole.setUserRole(UserRoleType.ADMIN);
                userRole.setUserEmail(gymAdmin.getAdminEmailAddress());
                userDao.addUserRole(userRole);
            }
            System.out.println("Admin added successfully");
        } catch (Exception e) {
            System.out.println("Error adding admin: " + e.getMessage());
        }
    }

    @Override
    public GymAdmin getAdminByEmail(String email) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_ADMIN_BY_EMAIL);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapAdminResultSet(rs);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public GymAdmin authenticate(String email, String password) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.AUTHENTICATE_ADMIN);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapAdminResultSet(rs);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    // ==================== GYM OWNER APPROVAL ====================

    @Override
    public List<GymOwner> viewPendingGymOwnerRequests() {
        List<GymOwner> owners = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_PENDING_GYM_OWNERS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                owners.add(mapOwnerResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return owners;
    }

    @Override
    public List<GymOwner> viewAllApprovedGymOwners() {
        List<GymOwner> owners = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_APPROVED_GYM_OWNERS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                owners.add(mapOwnerResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return owners;
    }

    @Override
    public boolean approveGymOwner(Long gymOwnerId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.APPROVE_GYM_OWNER);
            stmt.setLong(1, gymOwnerId);
            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean rejectGymOwner(Long gymOwnerId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.REJECT_GYM_OWNER);
            stmt.setLong(1, gymOwnerId);
            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    // ==================== GYM CENTER APPROVAL ====================

    @Override
    public List<GymCenter> viewPendingGymCenters() {
        List<GymCenter> centers = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_PENDING_GYM_CENTERS);
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
    public List<GymCenter> viewAllApprovedGymCenters() {
        List<GymCenter> centers = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_ALL_APPROVED_GYM_CENTERS);
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
    public boolean approveGymCenter(Long gymCenterId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.APPROVE_GYM_CENTER);
            stmt.setLong(1, gymCenterId);
            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean rejectGymCenter(Long gymCenterId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.REJECT_GYM_CENTER);
            stmt.setLong(1, gymCenterId);
            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    // ==================== HELPER METHODS ====================

    private GymAdmin mapAdminResultSet(ResultSet rs) throws Exception {
        GymAdmin admin = new GymAdmin();
        admin.setAdminId(rs.getLong("admin_id"));
        admin.setAdminName(rs.getString("name"));
        admin.setAdminEmailAddress(rs.getString("email"));
        admin.setPhone(rs.getString("phone"));
        admin.setPassword(rs.getString("password"));
        return admin;
    }

    private GymOwner mapOwnerResultSet(ResultSet rs) throws Exception {
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
