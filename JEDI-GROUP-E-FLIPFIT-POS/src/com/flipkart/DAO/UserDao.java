package com.flipkart.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.flipkart.bean.UserRole;
import com.flipkart.constants.Constants;
import com.flipkart.exceptions.UserNotFoundException;
import com.flipkart.utils.DB_utils;
import com.flipkart.utils.UserRoleType;

/**
 * UserDao - Implementation of UserDaoInterface
 * Manages user roles for authentication
 */
public class UserDao implements UserDaoInterface {
    
    public UserRole getUser(String userEmail) throws UserNotFoundException {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_USER_ROLE_BY_EMAIL);
            stmt.setString(1, userEmail);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                UserRole user = new UserRole();
                user.setId(rs.getLong("user_id"));
                user.setUserId(rs.getLong("user_id"));
                user.setUserEmail(rs.getString("user_email"));
                String role = rs.getString("user_role");
                user.setUserRole(UserRoleType.valueOf(role));
                return user;
            }
        } catch (Exception e) {
            System.out.println("Error getting user: " + e.getMessage());
        }
        return null;
    }

    public void addUserRole(UserRole userRole) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.ADD_USER_ROLE);
            stmt.setLong(1, userRole.getUserId());
            stmt.setString(2, userRole.getUserRole().toString());
            stmt.setString(3, userRole.getUserEmail());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Error adding user role: " + e.getMessage());
        }
    }
}
