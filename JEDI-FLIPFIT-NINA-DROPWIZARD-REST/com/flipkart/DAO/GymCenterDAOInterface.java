package com.flipkart.DAO;

import com.flipkart.bean.GymCenter;
import java.util.List;

/**
 * GymCenterDAOInterface - Gym center data access operations
 * Activity: "Add a new centre", "View Pending Centers", "Approve?", "Select City"
 */
public interface GymCenterDAOInterface {
    void addGymCenter(GymCenter gymCenter);
    GymCenter getGymCenterById(Long centerId);
    List<GymCenter> getAllApprovedGymCenters();
    List<GymCenter> getApprovedGymCentersByCity(String city);
    List<GymCenter> getPendingGymCenters();
    List<GymCenter> getGymCentersByOwner(Long ownerId);
    void approveGymCenter(Long centerId);
    void rejectGymCenter(Long centerId);
}
