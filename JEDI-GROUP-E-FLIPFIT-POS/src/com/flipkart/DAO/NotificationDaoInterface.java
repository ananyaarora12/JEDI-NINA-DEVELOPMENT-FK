package com.flipkart.DAO;

import com.flipkart.bean.Notification;
import java.util.List;

/**
 * NotificationDaoInterface - Notification data access operations
 * Activity: "Send rejection notification", "Promotion Notification", "Generate Success Notification"
 */
public interface NotificationDaoInterface {
    void createNotification(Notification notification);
    List<Notification> getNotificationsByUser(Long userId);
    List<Notification> getUnreadNotificationsByUser(Long userId);
    void markAsRead(Long notificationId);
}
