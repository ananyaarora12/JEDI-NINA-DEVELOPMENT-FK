package com.flipkart.DAO;

import java.util.List;
import com.flipkart.bean.GymAdmin;
import com.flipkart.bean.GymCenter;
import com.flipkart.bean.GymOwner;

/**
 * AdminDaoInterface - Admin data access operations
 * Activity: "Log in to FlipFit Admin", "View Pending Centers", "Approve?", "Delete Centre"
 */
public interface AdminDaoInterface {
    void addAdmin(GymAdmin gymAdmin);
    GymAdmin getAdminByEmail(String email);
    GymAdmin authenticate(String email, String password);
    
    // Gym Owner approval
    List<GymOwner> viewPendingGymOwnerRequests();
    List<GymOwner> viewAllApprovedGymOwners();
    boolean approveGymOwner(Long gymOwnerId);
    boolean rejectGymOwner(Long gymOwnerId);
    
    // Gym Center approval
    List<GymCenter> viewPendingGymCenters();
    List<GymCenter> viewAllApprovedGymCenters();
    boolean approveGymCenter(Long gymCenterId);
    boolean rejectGymCenter(Long gymCenterId);
}
