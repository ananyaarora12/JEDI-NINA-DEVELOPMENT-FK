package com.flipkart.DAO;

import com.flipkart.bean.Customer;

/**
 * GymCustomerDAOInterface - Customer data access operations
 * Activity: "Sign up", "Login", "Verify User Record & Fetch Profile"
 */
public interface GymCustomerDAOInterface {
    void addCustomer(Customer customer);
    Customer getCustomerByEmail(String email);
    Customer getCustomerById(Long customerId);
    Customer authenticate(String email, String password);
}
