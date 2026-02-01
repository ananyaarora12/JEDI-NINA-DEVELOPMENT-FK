package com.flipkart.DAO;

import com.flipkart.bean.WaitingList;
import java.util.List;

/**
 * WaitlistDAOInterface - Waitlist data access operations
 * Activity: "Add User to Waitlist", "Check Waitlist for Slot", "Select first user in queue (FIFO)"
 */
public interface WaitlistDAOInterface {
    void addToWaitlist(WaitingList entry);
    WaitingList getFirstInWaitlist(Long slotId);
    List<WaitingList> getWaitlistBySlot(Long slotId);
    int getWaitlistCount(Long slotId);
    void removeFromWaitlist(Long waitlistId);
    boolean isCustomerInWaitlist(Long customerId, Long slotId);
    int getCustomerPosition(Long customerId, Long slotId);
}
