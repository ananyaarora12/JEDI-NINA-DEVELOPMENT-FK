package com.flipkart.client;

import com.flipkart.DAO.*;
import com.flipkart.bean.*;
import com.flipkart.business.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * GymOwnerMenu - Gym Owner interface
 * 
 * From Activity Diagram (GymOwner):
 * - "Sign up" / "Login"
 * - "View Dashboard"
 * - "Add a new centre (Location, Name, Capacity)"
 * - "Submit gym details"
 * - "Configure Slots and Capacity"
 * - "Publish Slots to User App"
 */
public class GymOwnerMenu {
    
    private GymOwnerOperation gymOwnerOperations = new GymOwnerOperation();
    private GymCentreOperation gymCentreOperation = new GymCentreOperation();
    private SlotsDAOInterface slotsDAO = new SlotsDAO();
    private Scanner scanner = new Scanner(System.in);

    /**
     * Gym Owner Login
     * Activity: "Login" -> "Validate Credentials & Session"
     */
    public boolean gymOwnerLogin(String email, String password) {
        if (gymOwnerOperations.validUser(email, password)) {
            System.out.println("\n✓ Login successful!");
            GymOwner gymOwner = gymOwnerOperations.getGymOwnerByEmail(email);
            gymOwnerMainPage(gymOwner.getOwnerName(), gymOwner.getOwnerId());
            return true;
        } else {
            System.out.println("Invalid email or password!");
            return false;
        }
    }

    /**
     * Gym Owner Registration
     * Activity: "Sign up" if not registered
     */
    public void register() {
        System.out.println("\n----- Gym Owner Registration -----");
        
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        System.out.print("Enter PAN number: ");
        String panNumber = scanner.nextLine();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        gymOwnerOperations.createGymOwner(name, email, password, phone, panNumber, false, address);
        System.out.println("✓ Registration successful! Waiting for admin approval.\n");
    }

    /**
     * Gym Owner Main Menu
     * Activity: "View Dashboard"
     */
    public void gymOwnerMainPage(String ownerName, Long ownerId) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        
        System.out.println("\n========================================");
        printLoginHeader("Welcome, " + ownerName, "Login time: " + now.format(formatter));
        System.out.println("========================================");

        while (true) {
            System.out.println("\n----- Gym Owner Menu -----");
            System.out.println("1. View My Gym Centers");
            System.out.println("2. Add New Gym Center");
            System.out.println("3. View Slots for a Center");
            System.out.println("4. Add Slot to a Center");
            System.out.println("5. Remove a Slot");
            System.out.println("6. Logout");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewMyGymCenters(ownerId);
                    break;
                case 2:
                    addNewGymCenter(ownerId);
                    break;
                case 3:
                    viewSlotsForCenter();
                    break;
                case 4:
                    addSlotToCenter();
                    break;
                case 5:
                    removeSlot();
                    break;
                case 6:
                    System.out.println("Logging out...\n");
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    /**
     * View owner's gym centers
     */
    private void viewMyGymCenters(Long ownerId) {
        System.out.println("\n----- My Gym Centers -----");
        List<GymCenter> centers = gymCentreOperation.getAllGymCentersByGymOwnerId(ownerId);
        
        if (centers == null || centers.isEmpty()) {
            System.out.println("You have no gym centers. Add one!");
            return;
        }
        
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-6s %-20s %-15s %-10s %-15s%n", "ID", "Name", "City", "Capacity", "Status");
        System.out.println("------------------------------------------------------------");
        
        for (GymCenter gym : centers) {
            System.out.printf("%-6d %-20s %-15s %-10d %-15s%n",
                gym.getId(), gym.getName(), gym.getCity(), 
                gym.getCapacity(), gym.getStatus());
        }
        System.out.println("------------------------------------------------------------");
    }

    /**
     * Add new gym center
     * Activity: "Add a new centre (Location, Name, Capacity)" -> "Submit gym details"
     */
    private void addNewGymCenter(Long ownerId) {
        System.out.println("\n----- Add New Gym Center -----");
        
        System.out.print("Enter gym name: ");
        String name = scanner.nextLine();

        System.out.print("Enter city: ");
        String city = scanner.nextLine();

        System.out.print("Enter full address/location: ");
        String location = scanner.nextLine();

        System.out.print("Enter capacity (max people): ");
        int capacity = scanner.nextInt();
        scanner.nextLine();

        GymCenter gymCenter = new GymCenter();
        gymCenter.setName(name);
        gymCenter.setCity(city);
        gymCenter.setLocation(location);
        gymCenter.setCapacity(capacity);
        gymCenter.setStatus("PENDING");
        gymCenter.setGymOwnerId(ownerId);

        gymOwnerOperations.addCentre(gymCenter);
        System.out.println("✓ Gym center submitted for approval!");
    }

    /**
     * View slots for a center
     */
    private void viewSlotsForCenter() {
        System.out.print("Enter Center ID: ");
        Long centerId = scanner.nextLong();
        scanner.nextLine();
        
        System.out.println("\n----- Slots for Center " + centerId + " -----");
        List<Slot> slots = slotsDAO.getAllSlotsByGymCenterId(centerId);
        
        if (slots == null || slots.isEmpty()) {
            System.out.println("No slots configured for this center.");
            return;
        }
        
        System.out.println("------------------------------------------------------------------------");
        System.out.printf("%-8s %-12s %-10s %-10s %-10s %-10s %-10s%n", 
            "Slot ID", "Date", "Start", "End", "Total", "Available", "Price");
        System.out.println("------------------------------------------------------------------------");
        
        for (Slot slot : slots) {
            System.out.printf("%-8d %-12s %-10s %-10s %-10d %-10d ₹%-9d%n",
                slot.getSlotID(),
                slot.getDate(),
                slot.getStartTime(),
                slot.getEndTime(),
                slot.getTotalSeats(),
                slot.getAvailableSeats(),
                slot.getPrice());
        }
        System.out.println("------------------------------------------------------------------------");
    }

    /**
     * Add slot to a center
     * Activity: "Configure Slots and Capacity" -> "Publish Slots to User App"
     */
    private void addSlotToCenter() {
        System.out.println("\n----- Add New Slot -----");
        
        System.out.print("Enter Center ID: ");
        Long centerId = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Enter date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        Date date = Date.valueOf(dateStr);

        System.out.print("Enter start time (HH:MM): ");
        String startTime = scanner.nextLine();

        System.out.print("Enter end time (HH:MM): ");
        String endTime = scanner.nextLine();

        System.out.print("Enter total seats: ");
        int totalSeats = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter price per slot: ");
        int price = scanner.nextInt();
        scanner.nextLine();

        Slot slot = new Slot();
        slot.setCentreId(centerId);
        slot.setDate(date);
        slot.setStartTime(startTime);
        slot.setEndTime(endTime);
        slot.setTotalSeats(totalSeats);
        slot.setAvailableSeats(totalSeats);
        slot.setPrice(price);

        slotsDAO.addSlot(slot);
        System.out.println("✓ Slot added successfully!");
    }

    /**
     * Remove a slot
     */
    private void removeSlot() {
        System.out.print("Enter Slot ID to remove: ");
        Long slotId = scanner.nextLong();
        scanner.nextLine();
        
        slotsDAO.deleteSlot(slotId);
        System.out.println("✓ Slot removed successfully!");
    }

    private void printLoginHeader(String left, String right) {
        int width = 60;
        System.out.println(String.format("%-" + width + "s%20s", left, right));
    }
}
