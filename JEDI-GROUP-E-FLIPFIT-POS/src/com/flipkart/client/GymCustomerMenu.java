package com.flipkart.client;

import com.flipkart.DAO.GymCustomerDAO;
import com.flipkart.DAO.GymCustomerDAOInterface;
import com.flipkart.bean.BookSlot;
import com.flipkart.bean.Customer;
import com.flipkart.bean.GymCenter;
import com.flipkart.bean.Slot;
import com.flipkart.business.CustomerOperations;
import com.flipkart.exceptions.UserNotFoundException;
import com.flipkart.utils.BookingStatusType;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * GymCustomerMenu - Customer interface
 * 
 * From Activity Diagram (Customer):
 * - "Sign up" / "Login"
 * - "View Dashboard"
 * - "Select City" -> View gyms by city
 * - "Choose Date & Time Slot"
 * - "View My Plan"
 * - "Select Booking to Cancel"
 */
public class GymCustomerMenu {
   
    private CustomerOperations customerOperation = new CustomerOperations();
    private GymCustomerDAOInterface gymCustomerDAO = new GymCustomerDAO();
    private Scanner scanner = new Scanner(System.in);

    /**
     * Customer Login
     * Activity: "Login" -> "Validate Credentials & Session"
     */
    public boolean customerLogin(String email, String password) throws UserNotFoundException {
        if (customerOperation.validUser(email, password)) {
            System.out.println("\n✓ Login successful!");
            Customer customer = gymCustomerDAO.getCustomerByEmail(email);
            customerMainPage(customer.getName(), customer.getCustomerId());
            return true;
        } else {
            System.out.println("Invalid email or password!");
            return false;
        }
    }

    /**
     * Customer Registration
     * Activity: "Sign up" if not registered
     */
    public void registerCustomer() {
        System.out.println("\n----- Customer Registration -----");
        
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        System.out.print("Enter city: ");
        String city = scanner.nextLine();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        customerOperation.createCustomer(name, email, phone, password, city, address);
        System.out.println("✓ Registration successful! You can now login.\n");
    }

    /**
     * Customer Main Menu
     * Activity: "View Dashboard"
     */
    public void customerMainPage(String userName, Long customerId) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        
        System.out.println("\n========================================");
        printLoginHeader("Welcome, " + userName, "Login time: " + now.format(formatter));
        System.out.println("========================================");
        
        while (true) {
            System.out.println("\n----- Customer Menu -----");
            System.out.println("1. View All Gym Centers");
            System.out.println("2. View Gym Centers by City");
            System.out.println("3. View Slots for a Gym Center");
            System.out.println("4. Book a Slot");
            System.out.println("5. View My Bookings");
            System.out.println("6. Cancel a Booking");
            System.out.println("7. Logout");
            System.out.print("Enter choice: ");
            
            int option = scanner.nextInt();
            scanner.nextLine();
            
            switch (option) {
                case 1:
                    viewAllGymCenters();
                    break;
                case 2:
                    viewGymCentersByCity();
                    break;
                case 3:
                    viewSlotsForCenter();
                    break;
                case 4:
                    bookSlot(customerId);
                    break;
                case 5:
                    viewMyBookings(customerId);
                    break;
                case 6:
                    cancelBooking(customerId);
                    break;
                case 7:
                    System.out.println("Logging out...\n");
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    /**
     * View all approved gym centers
     */
    private void viewAllGymCenters() {
        System.out.println("\n----- All Approved Gym Centers -----");
        List<GymCenter> gymCenters = customerOperation.viewAllGymCenters();
        
        if (gymCenters == null || gymCenters.isEmpty()) {
            System.out.println("No gym centers available.");
            return;
        }
        
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-6s %-20s %-15s %-20s%n", "ID", "Name", "City", "Location");
        System.out.println("------------------------------------------------------------");
        
        for (GymCenter gym : gymCenters) {
            System.out.printf("%-6d %-20s %-15s %-20s%n",
                gym.getId(), gym.getName(), gym.getCity(), gym.getLocation());
        }
        System.out.println("------------------------------------------------------------");
    }

    /**
     * View gym centers by city
     * Activity: "Select City" -> "Request Centers for City" -> "Fetch Active Centers List"
     */
    private void viewGymCentersByCity() {
        System.out.print("Enter city name: ");
        String city = scanner.nextLine();
        
        System.out.println("\n----- Gym Centers in " + city + " -----");
        List<GymCenter> gymCenters = customerOperation.getGymCentersByCity(city);
        
        if (gymCenters == null || gymCenters.isEmpty()) {
            System.out.println("No gym centers found in " + city);
            return;
        }
        
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-6s %-20s %-15s %-20s%n", "ID", "Name", "City", "Location");
        System.out.println("------------------------------------------------------------");
        
        for (GymCenter gym : gymCenters) {
            System.out.printf("%-6d %-20s %-15s %-20s%n",
                gym.getId(), gym.getName(), gym.getCity(), gym.getLocation());
        }
        System.out.println("------------------------------------------------------------");
    }

    /**
     * View slots for a gym center
     * Activity: "Choose Date & Time Slot"
     */
    private void viewSlotsForCenter() {
        System.out.print("Enter Gym Center ID: ");
        Long centerId = scanner.nextLong();
        scanner.nextLine();

        Date slotDate = readDateInput("Enter date (YYYY-MM-DD): ");
        if (slotDate == null) {
            return;
        }

        displaySlotsForCenterAndDate(centerId, slotDate);
    }

    private void displaySlotsForCenterAndDate(Long centerId, Date slotDate) {
        System.out.println("\n----- Slots for " + slotDate + " -----");
        List<Slot> slots = customerOperation.getSlotsByCenterAndDate(centerId, slotDate);

        if (slots == null || slots.isEmpty()) {
            System.out.println("No slots available for this center on the selected date.");
            return;
        }

        System.out.println("------------------------------------------------------------------------------------");
        System.out.printf("%-8s %-12s %-8s %-8s %-10s %-8s %-10s%n", 
            "Slot ID", "Date", "Start", "End", "Available", "Total", "Status");
        System.out.println("------------------------------------------------------------------------------------");

        for (Slot slot : slots) {
            String status = slot.getAvailableSeats() > 0 ? "AVAILABLE" : "FULL";
            System.out.printf("%-8d %-12s %-8s %-8s %-10d %-8d %-10s%n",
                slot.getSlotID(), 
                slot.getDate(), 
                slot.getStartTime(), 
                slot.getEndTime(),
                slot.getAvailableSeats(),
                slot.getTotalSeats(),
                status);
        }
        System.out.println("------------------------------------------------------------------------------------");
    }

    /**
     * Book a slot
     * Activity: "Create Booking Record & Decrement Seat Count" OR "Add User to Waitlist"
     */
    private void bookSlot(Long customerId) {
        System.out.print("Enter Gym Center ID: ");
        Long centerId = scanner.nextLong();
        scanner.nextLine();

        Date slotDate = readDateInput("Enter date (YYYY-MM-DD): ");
        if (slotDate == null) {
            return;
        }

        displaySlotsForCenterAndDate(centerId, slotDate);

        System.out.print("Enter Slot ID to book: ");
        Long slotId = scanner.nextLong();
        scanner.nextLine();
        
        System.out.println("\nProcessing booking...");
        BookingStatusType result = customerOperation.bookSlot(customerId, slotId, slotDate);
        
        if (result == BookingStatusType.CONFIRMED) {
            System.out.println("✓ Booking confirmed!");
        } else if (result == BookingStatusType.WAITLISTED) {
            System.out.println("✓ Added to waitlist. You'll be notified when a seat is available.");
        } else {
            System.out.println("✗ Booking failed.");
        }
    }

    /**
     * View my bookings
     * Activity: "View My Plan"
     */
    private void viewMyBookings(Long customerId) {
        System.out.print("Enter date (YYYY-MM-DD) to filter or press Enter for all: ");
        String dateInput = scanner.nextLine().trim();
        List<BookSlot> bookings;
        if (dateInput.isEmpty()) {
            bookings = customerOperation.viewAllBooking(customerId);
        } else {
            Date slotDate = readDateInput(null, dateInput);
            if (slotDate == null) {
                return;
            }
            bookings = customerOperation.viewBookingsByDate(customerId, slotDate);
        }

        System.out.println("\n----- My Bookings -----");
        
        if (bookings == null || bookings.isEmpty()) {
            System.out.println("You have no bookings.");
            return;
        }
        
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-10s %-10s %-12s %-15s%n", "Booking ID", "Slot ID", "Date", "Status");
        System.out.println("------------------------------------------------------------");
        
        for (BookSlot booking : bookings) {
            System.out.printf("%-10d %-10d %-12s %-15s%n",
                booking.getBookingId(),
                booking.getSlotId(),
                booking.getSlotDate(),
                booking.getBookingStatus());
        }
        System.out.println("------------------------------------------------------------");
    }

    /**
     * Cancel a booking
     * Activity: "Select Booking to Cancel" -> "Delete Booking & Increment Seat Count"
     */
    private void cancelBooking(Long customerId) {
        // First show bookings
        viewMyBookings(customerId);
        
        System.out.print("Enter Booking ID to cancel: ");
        Long bookingId = scanner.nextLong();
        scanner.nextLine();
        
        customerOperation.cancelBooking(bookingId);
        System.out.println("✓ Booking cancelled successfully!");
    }

    private Date readDateInput(String prompt) {
        if (prompt != null) {
            System.out.print(prompt);
        }
        String dateInput = scanner.nextLine().trim();
        return readDateInput(prompt, dateInput);
    }

    private Date readDateInput(String prompt, String dateInput) {
        try {
            return Date.valueOf(dateInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Use YYYY-MM-DD.");
            return null;
        }
    }

    private void printLoginHeader(String left, String right) {
        int width = 60;
        System.out.println(String.format("%-" + width + "s%20s", left, right));
    }
}
