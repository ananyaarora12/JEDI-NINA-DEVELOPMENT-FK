package com.flipkart.business;

import com.flipkart.bean.*;
import com.flipkart.DAO.PaymentDAO;
import com.flipkart.DAO.PaymentDaoInterface;
import com.flipkart.utils.PaymentStatusType;
import com.flipkart.utils.PaymentType;
import java.util.Scanner;

public class PaymentOperation {
	// Method to process the payment for a customer for a specific slot
    public void processPayment(Customer customer, Slot slot) {
        // Check if the slot is available for booking
        if (!checkSlotAvailability(slot)) {
            System.out.println("Slot not available. Adding to waiting list.");
            return; // Exit if slot is not available
        }

        // Create a scanner object to take user input for payment method
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select payment method:");
        System.out.println("1. UPI");
        System.out.println("2. Credit Card");
        System.out.println("3. Debit Card");

        // Take the user's choice for payment method
        int choice = scanner.nextInt();
        
        // Boolean to track if payment is successful
        boolean paymentSuccess = false;
        switch (choice) {
            case 1:
                // Call method for UPI payment
                paymentSuccess = processUPIPayment();
                break;
            case 2:
                // Call method for Credit Card payment
                paymentSuccess = processCardPayment("Credit");
                break;
            case 3:
                // Call method for Debit Card payment
                paymentSuccess = processCardPayment("Debit");
                break;
            default:
                // Handle invalid choice
                System.out.println("Invalid choice");
                return; // Exit the method
        }

        // If payment is successful, proceed to book the slot
        if (paymentSuccess) {
            System.out.println("Payment successful!");
            bookSlot(customer, slot);
        } else {
            // If payment failed, display failure message
            System.out.println("Payment failed!");
        }
    }

    // Method to check if the selected slot is available for booking
    public boolean checkSlotAvailability(Slot slot) {
        // Basic check: if slot's waitlist is null, it's considered available
        return slot.getWaitList() == null;
    }

    // Method to simulate UPI payment processing
    public boolean processUPIPayment() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter UPI ID:");
        String upiId = scanner.nextLine(); // Capture UPI ID from user input
        // Simulate successful UPI payment processing
        return true;
    }

    // Method to simulate processing a card payment (Credit or Debit)
    public boolean processCardPayment(String cardType) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter " + cardType + " Card Number:");
        String cardNumber = scanner.nextLine(); // Capture card number from user input
        // Simulate successful card payment processing
        return true;
    }

    // Method to book a slot for the customer
    public void bookSlot(Customer customer, Slot slot) {
        // Display a message indicating that the slot has been booked for the customer
        System.out.println("Slot booked for customer: " + customer.getCustomerName());
    }

    // Method to process a refund for the customer
    public void processRefund(Customer customer, Slot slot) {
        // Display messages indicating that the refund is being processed
        System.out.println("Processing refund for customer: " + customer.getCustomerName());
        System.out.println("Refund processed successfully");
    }
    
    // Instantiate the PaymentDAO interface to interact with the database for payment processing
    PaymentDaoInterface paymentDao = new PaymentDAO();
    
    // Method to make a payment, passing card number and customer email
    public void makePayment(Long cardNumber, String customerEmail) {
        // Call the DAO's makePayment method to process the payment
        paymentDao.makePayment(cardNumber, customerEmail);
        // Display a success message after payment processing
        System.out.println("Payment Successful .....");
    }
}

