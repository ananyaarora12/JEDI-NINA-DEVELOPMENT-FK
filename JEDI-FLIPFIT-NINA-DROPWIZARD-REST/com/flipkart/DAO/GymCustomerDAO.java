package com.flipkart.DAO;

import com.flipkart.bean.Customer;
import com.flipkart.bean.UserRole;
import com.flipkart.constants.Constants;
import com.flipkart.utils.DB_utils;
import com.flipkart.utils.UserRoleType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * GymCustomerDAO - Implementation of GymCustomerDAOInterface
 * Activity: "Sign up", "Login", "Verify User Record & Fetch Profile"
 */
public class GymCustomerDAO implements GymCustomerDAOInterface {

    private UserDaoInterface userDao = new UserDao();

    @Override
    public void addCustomer(Customer customer) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.ADD_CUSTOMER);
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPhone());
            stmt.setString(4, customer.getPassword());
            stmt.setString(5, customer.getCity());
            stmt.setString(6, customer.getAddress());
            stmt.executeUpdate();
            stmt.close();
            
            // Add user role
            Customer saved = getCustomerByEmail(customer.getEmail());
            if (saved != null) {
                UserRole userRole = new UserRole();
                userRole.setUserId(saved.getCustomerId());
                userRole.setUserRole(UserRoleType.CUSTOMER);
                userRole.setUserEmail(customer.getEmail());
                userDao.addUserRole(userRole);
            }
        } catch (Exception e) {
            System.out.println("Error adding customer: " + e.getMessage());
        }
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_CUSTOMER_BY_EMAIL);
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
    public Customer getCustomerById(Long customerId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_CUSTOMER_BY_ID);
            stmt.setLong(1, customerId);
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
    public Customer authenticate(String email, String password) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.AUTHENTICATE_CUSTOMER);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    private Customer mapResultSet(ResultSet rs) throws Exception {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getLong("customer_id"));
        customer.setName(rs.getString("name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setPassword(rs.getString("password"));
        customer.setCity(rs.getString("city"));
        customer.setAddress(rs.getString("address"));
        return customer;
    }
}
