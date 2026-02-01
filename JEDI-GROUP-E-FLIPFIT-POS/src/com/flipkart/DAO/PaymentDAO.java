package com.flipkart.DAO;

import com.flipkart.utils.DB_utils;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * PaymentDAO - Implementation of PaymentDaoInterface
 * Handles payment processing (placeholder implementation)
 */
public class PaymentDAO implements PaymentDaoInterface {

    @Override
    public void makePayment(Long cardNumber, String customerEmail) {
        try {
            Connection connection = DB_utils.getConnection();
            // Placeholder - in a real system, you would process the payment
            System.out.println("Processing payment for: " + customerEmail);
            System.out.println("Payment successful!");
        } catch (Exception e) {
            System.out.println("Payment error: " + e.getMessage());
        }
    }
}
