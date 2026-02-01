/**
 * GymCentreOperation class manages gym center operations,
 * including adding, retrieving, updating, and deleting gym centers and slots.
 */
package com.flipkart.business;

import com.flipkart.DAO.GymCenterDAO;
import com.flipkart.DAO.GymCenterDAOInterface;
import com.flipkart.DAO.SlotsDAO;
import com.flipkart.DAO.SlotsDAOInterface;
import com.flipkart.bean.GymCenter;
import com.flipkart.bean.Slot;

import java.util.List;

public class GymCentreOperation {
    
    private SlotsDAOInterface slotsDAO = new SlotsDAO();
    private GymCenterDAOInterface gymCenterDAO = new GymCenterDAO();
    
    /**
     * Retrieves a gym center by its ID.
     */
    public GymCenter getCentre(Long centerId) {
        return gymCenterDAO.getGymCenterById(centerId);
    }

    /**
     * Adds a new gym center to the system.
     */
    public GymCenter addCentre(GymCenter centre) {
        gymCenterDAO.addGymCenter(centre);
        return centre;
    }

    /**
     * Adds a new slot to a specified gym center.
     */
    public Slot addSlot(Long centreId, Slot slot) {
        slot.setCentreId(centreId);
        slotsDAO.addSlot(slot);
        return slot;
    }

    /**
     * Retrieves all slots for a given gym center.
     */
    public List<Slot> getAllSlots(Long centreId) {
        return slotsDAO.getAllSlotsByGymCenterId(centreId);
    }

    /**
     * Retrieves a slot by its ID.
     */
    public Slot getSlotById(Long slotId) {
        return slotsDAO.getSlotById(slotId);
    }

    /**
     * Retrieves all approved gym centers.
     */
    public List<GymCenter> getAllGymCenters() {
        return gymCenterDAO.getAllApprovedGymCenters();
    }

    /**
     * Retrieves all gym centers owned by a specific gym owner.
     */
    public List<GymCenter> getAllGymCentersByGymOwnerId(Long gymOwnerId) {
        return gymCenterDAO.getGymCentersByOwner(gymOwnerId);
    }
    
    /**
     * Retrieves approved gym centers by city.
     */
    public List<GymCenter> getGymCentersByCity(String city) {
        return gymCenterDAO.getApprovedGymCentersByCity(city);
    }
}
