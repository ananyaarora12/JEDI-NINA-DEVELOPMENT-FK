package com.flipkart.client;

import com.flipkart.business.AdminOperation;
import com.flipkart.bean.GymAdmin;
import com.flipkart.bean.GymCenter;
import com.flipkart.bean.GymOwner;
import com.flipkart.exceptions.UserNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * GymAdminMenu - Admin interface
 * 
 * From Activity Diagram (Admin):
 * - "Log in to FlipFit Admin" -> "Authenticate Credentials"
 * - "View Pending Centers" -> "Fetch Gym Owner Requests"
 * - "Approve?" -> [YES] "Save Center & Slot Info" / [NO] "Delete Centre"
 * - "Send rejection notification"
 */
public class GymAdminMenu {
    
    private AdminOperation adminOperation = new AdminOperation();
    private Scanner scanner = new Scanner(System.in);

    /**
     * Admin Login
     * Activity: "Log in to FlipFit Admin" -> "Authenticate Credentials"
     */
    public boolean adminLogin(String email, String password) throws UserNotFoundException {
        if (adminOperation.validUser(email, password)) {
            System.out.println("\n✓ Login successful!");
            GymAdmin admin = adminOperation.getAdminByEmail(email);
            adminMainPage(admin.getAdminName());
            return true;
        } else {
            System.out.println("Invalid email or password!");
            return false;
        }
    }

    /**
     * Admin Registration
     */
    public boolean adminRegister() {
        System.out.println("\n----- Admin Registration -----");
        
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        adminOperation.createAdmin(name, email, phone, password);
        System.out.println("✓ Admin registered successfully!\n");
        return true;
    }

    /**
     * Admin Main Menu
     * Activity: After successful login, admin can view/approve/reject
     */
    public void adminMainPage(String username) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        
        System.out.println("\n========================================");
        printLoginHeader("Welcome, Admin " + username, "Login time: " + now.format(formatter));
        System.out.println("========================================");

        while (true) {
            System.out.println("\n----- Admin Menu -----");
            System.out.println("1. View Pending Gym Owners");
            System.out.println("2. View Pending Gym Centers");
            System.out.println("3. View Approved Gym Owners");
            System.out.println("4. View Approved Gym Centers");
            System.out.println("5. Filter Gym Owners (Approved/Not Approved)");
            System.out.println("6. Filter Gym Centers (Approved/Not Approved)");
            System.out.println("7. Approve Gym Owner");
            System.out.println("8. Reject Gym Owner");
            System.out.println("9. Approve Gym Center");
            System.out.println("10. Reject Gym Center");
            System.out.println("11. Logout");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewPendingGymOwners();
                    break;
                case 2:
                    viewPendingCenters();
                    break;
                case 3:
                    viewApprovedGymOwners();
                    break;
                case 4:
                    viewApprovedCenters();
                    break;
                case 5:
                    filterGymOwners();
                    break;
                case 6:
                    filterGymCenters();
                    break;
                case 7:
                    approveGymOwner();
                    break;
                case 8:
                    rejectGymOwner();
                    break;
                case 9:
                    approveGymCenter();
                    break;
                case 10:
                    rejectGymCenter();
                    break;
                case 11:
                    System.out.println("Logging out...\n");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    /**
     * View Pending Gym Owners
     * Activity: "Fetch Gym Owner Requests"
     */
    private void viewPendingGymOwners() {
        System.out.println("\n----- Pending Gym Owners -----");
        List<GymOwner> owners = adminOperation.viewPendingGymOwners();
        
        if (owners == null || owners.isEmpty()) {
            System.out.println("No pending gym owner requests.");
            return;
        }
        
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-6s %-15s %-25s %-15s%n", "ID", "Name", "Email", "PAN");
        System.out.println("------------------------------------------------------------");
        
        for (GymOwner owner : owners) {
            System.out.printf("%-6d %-15s %-25s %-15s%n",
                owner.getOwnerId(), owner.getOwnerName(), 
                owner.getOwnerEmailAddress(), owner.getOwnerPanNum());
        }
        System.out.println("------------------------------------------------------------");
    }

    /**
     * View Pending Gym Centers
     * Activity: "View Pending Centers"
     */
    private void viewPendingCenters() {
        System.out.println("\n----- Pending Gym Centers -----");
        List<GymCenter> centers = adminOperation.viewPendingGymCentres();
        
        if (centers == null || centers.isEmpty()) {
            System.out.println("No pending gym center requests.");
            return;
        }
        
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-6s %-20s %-15s %-15s%n", "ID", "Name", "City", "Status");
        System.out.println("------------------------------------------------------------");
        
        for (GymCenter gym : centers) {
            System.out.printf("%-6d %-20s %-15s %-15s%n",
                gym.getId(), gym.getName(), gym.getCity(), gym.getStatus());
        }
        System.out.println("------------------------------------------------------------");
    }

    /**
     * View Approved Gym Owners
     */
    private void viewApprovedGymOwners() {
        System.out.println("\n----- Approved Gym Owners -----");
        List<GymOwner> owners = adminOperation.viewApprovedGymOwners();
        
        if (owners == null || owners.isEmpty()) {
            System.out.println("No approved gym owners.");
            return;
        }
        
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-6s %-15s %-25s %-15s%n", "ID", "Name", "Email", "PAN");
        System.out.println("------------------------------------------------------------");
        
        for (GymOwner owner : owners) {
            System.out.printf("%-6d %-15s %-25s %-15s%n",
                owner.getOwnerId(), owner.getOwnerName(), 
                owner.getOwnerEmailAddress(), owner.getOwnerPanNum());
        }
        System.out.println("------------------------------------------------------------");
    }

    /**
     * View Approved Gym Centers
     */
    private void viewApprovedCenters() {
        System.out.println("\n----- Approved Gym Centers -----");
        List<GymCenter> centers = adminOperation.viewApprovedGymCentres();
        
        if (centers == null || centers.isEmpty()) {
            System.out.println("No approved gym centers.");
            return;
        }
        
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-6s %-20s %-15s %-15s%n", "ID", "Name", "City", "Status");
        System.out.println("------------------------------------------------------------");
        
        for (GymCenter gym : centers) {
            System.out.printf("%-6d %-20s %-15s %-15s%n",
                gym.getId(), gym.getName(), gym.getCity(), gym.getStatus());
        }
        System.out.println("------------------------------------------------------------");
    }

    private void filterGymOwners() {
        System.out.print("Show approved owners only? (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        boolean approvedOnly = input.equals("y") || input.equals("yes");
        List<GymOwner> owners = adminOperation.filterGymOwnersByApproval(approvedOnly);
        if (approvedOnly) {
            System.out.println("\n----- Approved Gym Owners (Stream Filter) -----");
        } else {
            System.out.println("\n----- Not Approved Gym Owners (Stream Filter) -----");
        }
        displayGymOwners(owners);
    }

    private void filterGymCenters() {
        System.out.print("Show approved centers only? (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        boolean approvedOnly = input.equals("y") || input.equals("yes");
        List<GymCenter> centers = adminOperation.filterGymCentersByApproval(approvedOnly);
        if (approvedOnly) {
            System.out.println("\n----- Approved Gym Centers (Stream Filter) -----");
        } else {
            System.out.println("\n----- Not Approved Gym Centers (Stream Filter) -----");
        }
        displayGymCenters(centers);
    }

    /**
     * Approve Gym Owner
     */
    private void approveGymOwner() {
        viewPendingGymOwners();
        System.out.print("Enter Owner ID to approve: ");
        Long ownerId = scanner.nextLong();
        scanner.nextLine();
        
        if (adminOperation.approveGymOwner(ownerId)) {
            System.out.println("✓ Gym Owner approved successfully!");
        } else {
            System.out.println("✗ Failed to approve gym owner.");
        }
    }

    /**
     * Reject Gym Owner
     */
    private void rejectGymOwner() {
        viewPendingGymOwners();
        System.out.print("Enter Owner ID to reject: ");
        Long ownerId = scanner.nextLong();
        scanner.nextLine();
        
        if (adminOperation.rejectGymOwner(ownerId)) {
            System.out.println("✓ Gym Owner rejected. Notification sent.");
        } else {
            System.out.println("✗ Failed to reject gym owner.");
        }
    }

    /**
     * Approve Gym Center
     * Activity: "Approve?" -> [YES] -> "Save Center & Slot Info"
     */
    private void approveGymCenter() {
        viewPendingCenters();
        System.out.print("Enter Center ID to approve: ");
        Long centerId = scanner.nextLong();
        scanner.nextLine();
        
        if (adminOperation.approveGymCenter(centerId)) {
            System.out.println("✓ Gym Center approved successfully!");
        } else {
            System.out.println("✗ Failed to approve gym center.");
        }
    }

    /**
     * Reject Gym Center
     * Activity: "Approve?" -> [NO] -> "Delete Centre" -> "Send rejection notification"
     */
    private void rejectGymCenter() {
        viewPendingCenters();
        System.out.print("Enter Center ID to reject: ");
        Long centerId = scanner.nextLong();
        scanner.nextLine();
        
        if (adminOperation.rejectGymCenter(centerId)) {
            System.out.println("✓ Gym Center rejected. Notification sent to owner.");
        } else {
            System.out.println("✗ Failed to reject gym center.");
        }
    }

    private void displayGymOwners(List<GymOwner> owners) {
        if (owners == null || owners.isEmpty()) {
            System.out.println("No gym owners found.");
            return;
        }
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-6s %-15s %-25s %-15s%n", "ID", "Name", "Email", "PAN");
        System.out.println("------------------------------------------------------------");
        for (GymOwner owner : owners) {
            System.out.printf("%-6d %-15s %-25s %-15s%n",
                owner.getOwnerId(), owner.getOwnerName(),
                owner.getOwnerEmailAddress(), owner.getOwnerPanNum());
        }
        System.out.println("------------------------------------------------------------");
    }

    private void displayGymCenters(List<GymCenter> centers) {
        if (centers == null || centers.isEmpty()) {
            System.out.println("No gym centers found.");
            return;
        }
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-6s %-20s %-15s %-15s%n", "ID", "Name", "City", "Status");
        System.out.println("------------------------------------------------------------");
        for (GymCenter gym : centers) {
            System.out.printf("%-6d %-20s %-15s %-15s%n",
                gym.getId(), gym.getName(), gym.getCity(), gym.getStatus());
        }
        System.out.println("------------------------------------------------------------");
    }

    private void printLoginHeader(String left, String right) {
        int width = 60;
        System.out.println(String.format("%-" + width + "s%20s", left, right));
    }
}
