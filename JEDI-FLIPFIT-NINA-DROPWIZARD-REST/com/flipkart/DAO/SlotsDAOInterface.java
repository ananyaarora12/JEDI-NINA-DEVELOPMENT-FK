package com.flipkart.DAO;

import com.flipkart.bean.Slot;
import java.sql.Date;
import java.util.List;

/**
 * SlotsDAOInterface - Slot data access operations
 * Activity: "Configure Slots and Capacity", "Choose Date & Time Slot", "Decrement/Increment Seat Count"
 */
public interface SlotsDAOInterface {
    void addSlot(Slot slot);
    Slot getSlotById(Long slotId);
    List<Slot> getAllSlotsByGymCenterId(Long gymCenterId);
    List<Slot> getSlotsByCenterAndDate(Long centerId, Date date);
    List<Slot> getAvailableSlotsByCenter(Long centerId);
    List<Slot> getBookedSlotsByCustomerAndDate(Long customerId, Date date);
    boolean decrementSeatCount(Long slotId);
    boolean incrementSeatCount(Long slotId);
    void deleteSlot(Long slotId);
}
