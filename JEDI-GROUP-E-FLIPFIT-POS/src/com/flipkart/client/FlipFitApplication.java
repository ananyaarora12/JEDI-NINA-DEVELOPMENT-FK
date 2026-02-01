package com.flipkart.client;

import java.util.Objects;
import java.util.Scanner;

import com.flipkart.DAO.UserDao;
import com.flipkart.DAO.UserDaoInterface;
import com.flipkart.utils.UserRoleType;

/**
 * FlipFitApplication - Main entry point
 * 
 * From Activity Diagrams:
 * - "Launch FlipFit App"
 * - "Already registered?" -> Login / Sign up
 */
public class FlipFitApplication {
    
    public static Scanner scanner = new Scanner(System.in);
    private static GymAdminMenu adminClient = new GymAdminMenu();
    private static GymCustomerMenu customerClient = new GymCustomerMenu();
    private static GymOwnerMenu gymOwnerClient = new GymOwnerMenu();

    private static void mainPage() {
        System.out.println("\nWelcome to FlipFit App");
        System.out.println("- Login");
        System.out.println("- Register as GymOwner");
        System.out.println("- Register as GymCustomer");
        System.out.println("- Exit");
        System.out.print("Enter choice (1-4): ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                gymOwnerClient.register();
                break;
            case 3:
                customerClient.registerCustomer();
                break;
            case 4:
                System.out.println("Thank you for using FlipFit!");
                return;
            default:
                System.out.println("Invalid choice!");
                break;
        }
        mainPage();
    }

    private static void login() {
        try {
            System.out.print("Enter email: ");
            String email = scanner.next();
            
            System.out.print("Enter password: ");
            String password = scanner.next();
            
            UserDaoInterface userDao = new UserDao();
            com.flipkart.bean.UserRole user = userDao.getUser(email);
            
            if (Objects.isNull(user) || Objects.isNull(user.getUserRole())) {
                System.out.println("User not found! Please register first.");
                return;
            }
            
            UserRoleType role = user.getUserRole();
            
            switch (role) {
                case ADMIN:
                    adminClient.adminLogin(email, password);
                    break;
                case OWNER:
                    gymOwnerClient.gymOwnerLogin(email, password);
                    break;
                case CUSTOMER:
                    customerClient.customerLogin(email, password);
                    break;
                default:
                    System.out.println("Invalid role!");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║     Welcome to FlipFit Application    ║");
        System.out.println("║        Gym Slot Booking System        ║");
        System.out.println("╚═══════════════════════════════════════╝");
        mainPage();
    }
}