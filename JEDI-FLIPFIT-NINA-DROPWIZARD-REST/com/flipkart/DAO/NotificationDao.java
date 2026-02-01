package com.flipkart.DAO;

import com.flipkart.bean.Notification;
import com.flipkart.constants.Constants;
import com.flipkart.utils.DB_utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * NotificationDao - Implementation of NotificationDaoInterface
 * Activity: "Send rejection notification", "Promotion Notification", "Generate Success Notification"
 */
public class NotificationDao implements NotificationDaoInterface {

    @Override
    public void createNotification(Notification notification) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.ADD_NOTIFICATION);
            stmt.setLong(1, notification.getUserId());
            stmt.setString(2, notification.getMessage());
            stmt.setString(3, notification.getType());
            stmt.setTimestamp(4, notification.getCreatedAt());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Error creating notification: " + e.getMessage());
        }
    }

    @Override
    public List<Notification> getNotificationsByUser(Long userId) {
        List<Notification> notifications = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_NOTIFICATIONS_BY_USER);
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return notifications;
    }

    @Override
    public List<Notification> getUnreadNotificationsByUser(Long userId) {
        List<Notification> notifications = new ArrayList<>();
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.GET_UNREAD_NOTIFICATIONS);
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return notifications;
    }

    @Override
    public void markAsRead(Long notificationId) {
        try {
            Connection connection = DB_utils.getConnection();
            PreparedStatement stmt = connection.prepareStatement(Constants.MARK_NOTIFICATION_READ);
            stmt.setLong(1, notificationId);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Notification mapResultSet(ResultSet rs) throws Exception {
        Notification notification = new Notification();
        notification.setNotificationId(rs.getLong("notification_id"));
        notification.setUserId(rs.getLong("user_id"));
        notification.setMessage(rs.getString("message"));
        notification.setType(rs.getString("type"));
        notification.setRead(rs.getBoolean("is_read"));
        notification.setCreatedAt(rs.getTimestamp("created_at"));
        return notification;
    }
}
