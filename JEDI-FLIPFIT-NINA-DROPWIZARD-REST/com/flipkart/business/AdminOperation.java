package com.flipkart.business;

import com.flipkart.DAO.*;
import com.flipkart.bean.*;
import com.flipkart.exceptions.UserNotFoundException;
import com.flipkart.utils.NotificationType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * AdminOperation - Business logic for Admin
 * 
 * From Activity Diagram (Admin):
 * 1. "Log in to FlipFit Admin" -> "Authenticate Credentials"
 * 2. "Fetch Gym Owner Requests"
 * 3. "View Pending Centers"
 * 4. "Approve?" [yes] -> "Save Center & Slot Info"
 * 5. "Approve?" [no] -> "Delete Centre" -> "Cascading Delete" -> "Send rejection notification"
 */
public class AdminOperation {

    private AdminDaoInterface adminDao = new AdminDao();
    private NotificationDaoInterface notificationDAO = new NotificationDao();
    private GymCenterDAOInterface gymCenterDAO = new GymCenterDAO();

    public GymAdmin createAdmin(String name, String email, String phone, String password) {
        GymAdmin admin = new GymAdmin();
        admin.setAdminName(name);
        admin.setAdminEmailAddress(email);
        admin.setPhone(phone);
        admin.setPassword(password);
        adminDao.addAdmin(admin);
        return admin;
    }

    public boolean validUser(String email, String password) throws UserNotFoundException {
        GymAdmin admin = adminDao.getAdminByEmail(email);
        if (Objects.isNull(admin) || Objects.isNull(admin.getAdminId())) {
            throw new UserNotFoundException(email);
        }
        return Objects.equals(admin.getPassword(), password);
    }

    public GymAdmin getAdminByEmail(String email) {
        return adminDao.getAdminByEmail(email);
    }

    // ==================== GYM OWNER APPROVAL ====================
    
    public List<GymOwner> viewPendingGymOwners() {
        return adminDao.viewPendingGymOwnerRequests();
    }

    public List<GymOwner> viewApprovedGymOwners() {
        return adminDao.viewAllApprovedGymOwners();
    }

    public List<GymOwner> filterGymOwnersByApproval(boolean approved) {
        List<GymOwner> allOwners = new ArrayList<>();
        allOwners.addAll(adminDao.viewPendingGymOwnerRequests());
        allOwners.addAll(adminDao.viewAllApprovedGymOwners());
        return allOwners.stream()
            .filter(owner -> owner.isApproved() == approved)
            .collect(Collectors.toList());
    }

    public boolean approveGymOwner(long ownerId) {
        boolean approved = adminDao.approveGymOwner(ownerId);
        if (approved) {
            sendNotification(ownerId, "Your gym owner registration has been approved!", NotificationType.GYM_APPROVED);
        }
        return approved;
    }

    public boolean rejectGymOwner(long ownerId) {
        sendNotification(ownerId, "Your gym owner registration has been rejected.", NotificationType.GYM_REJECTED);
        return adminDao.rejectGymOwner(ownerId);
    }

    // ==================== GYM CENTER APPROVAL ====================
    
    public List<GymCenter> viewPendingGymCentres() {
        return adminDao.viewPendingGymCenters();
    }

    public List<GymCenter> viewApprovedGymCentres() {
        return adminDao.viewAllApprovedGymCenters();
    }

    public List<GymCenter> filterGymCentersByApproval(boolean approved) {
        List<GymCenter> allCenters = new ArrayList<>();
        allCenters.addAll(adminDao.viewPendingGymCenters());
        allCenters.addAll(adminDao.viewAllApprovedGymCenters());
        return allCenters.stream()
            .filter(center -> approved == "APPROVED".equalsIgnoreCase(center.getStatus()))
            .collect(Collectors.toList());
    }

    public boolean approveGymCenter(long centerId) {
        boolean approved = adminDao.approveGymCenter(centerId);
        if (approved) {
            GymCenter center = gymCenterDAO.getGymCenterById(centerId);
            if (center != null) {
                sendNotification(center.getGymOwnerId(), "Your gym center '" + center.getName() + "' has been approved!", NotificationType.GYM_APPROVED);
            }
        }
        return approved;
    }

    public boolean rejectGymCenter(long centerId) {
        GymCenter center = gymCenterDAO.getGymCenterById(centerId);
        if (center != null) {
            sendNotification(center.getGymOwnerId(), "Your gym center '" + center.getName() + "' has been rejected.", NotificationType.GYM_REJECTED);
        }
        return adminDao.rejectGymCenter(centerId);
    }

    private void sendNotification(Long userId, String message, NotificationType type) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setType(type.toString());
        notification.setRead(false);
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        notificationDAO.createNotification(notification);
    }
}
