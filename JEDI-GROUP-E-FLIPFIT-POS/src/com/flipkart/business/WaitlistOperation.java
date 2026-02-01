package com.flipkart.business;

import com.flipkart.DAO.WaitlistDAO;
import com.flipkart.DAO.WaitlistDAOInterface;
import com.flipkart.bean.WaitingList;

import java.util.List;

/**
 * WaitlistOperation - Business logic for waitlist management
 * 
 * From Activity Diagram (Customer):
 * - "Add User to Waitlist" when no seats available
 * - "Check Waitlist for Slot" when booking cancelled
 * - "Select first user in queue (FIFO)"
 */
public class WaitlistOperation {

    private WaitlistDAOInterface waitlistDAO = new WaitlistDAO();

    /**
     * Add a customer to the waitlist for a specific slot
     */
    public void addToWaitlist(Long customerId, Long slotId) {
        WaitingList entry = new WaitingList();
        entry.setCustomerId(customerId);
        entry.setSlotId(slotId);
        waitlistDAO.addToWaitlist(entry);
        System.out.println("Customer added to waitlist for slot: " + slotId);
    }

    /**
     * Get the first customer in the waitlist (FIFO)
     */
    public WaitingList getNextInQueue(Long slotId) {
        return waitlistDAO.getFirstInWaitlist(slotId);
    }

    /**
     * View the entire waitlist for a slot
     */
    public List<WaitingList> viewWaitlist(Long slotId) {
        return waitlistDAO.getWaitlistBySlot(slotId);
    }

    /**
     * Check if waitlist is empty for a slot
     */
    public boolean isWaitlistEmpty(Long slotId) {
        return waitlistDAO.getWaitlistCount(slotId) == 0;
    }

    /**
     * Get waitlist count for a slot
     */
    public int getWaitlistCount(Long slotId) {
        return waitlistDAO.getWaitlistCount(slotId);
    }

    /**
     * Remove a customer from the waitlist
     */
    public void removeFromWaitlist(Long waitlistId) {
        waitlistDAO.removeFromWaitlist(waitlistId);
    }

    /**
     * Check if customer is already in waitlist
     */
    public boolean isCustomerInWaitlist(Long customerId, Long slotId) {
        return waitlistDAO.isCustomerInWaitlist(customerId, slotId);
    }

    /**
     * Get customer's position in the waitlist
     */
    public int getCustomerPosition(Long customerId, Long slotId) {
        return waitlistDAO.getCustomerPosition(customerId, slotId);
    }
}
