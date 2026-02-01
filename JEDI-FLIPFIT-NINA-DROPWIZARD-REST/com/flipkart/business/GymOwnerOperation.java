/**
 * GymOwnerOperation class handles operations related to gym owners,
 * including managing gym centers, slots, bookings, and authentication.
 */
package com.flipkart.business;

import com.flipkart.DAO.OwnerDAO;
import com.flipkart.DAO.OwnerDAOInterface;
import com.flipkart.DAO.SlotsDAO;
import com.flipkart.DAO.SlotsDAOInterface;
import com.flipkart.bean.BookSlot;
import com.flipkart.bean.GymCenter;
import com.flipkart.bean.GymOwner;
import com.flipkart.bean.Slot;

import java.util.List;
import java.util.Objects;

public class GymOwnerOperation {

    // DAO instances to interact with the database
    private OwnerDAOInterface ownerDAO = new OwnerDAO();
    private SlotsDAOInterface slotsDAO = new SlotsDAO();

    /**
     * Adds a new gym center owned by the gym owner.
     * 
     * @param gymCenter GymCenter object to be added
     */
    public void addCentre(GymCenter gymCenter){
        ownerDAO.addCenter(gymCenter);
    }

    /**
     * Adds a new slot to a gym center.
     * 
     * @param slot Slot object to be added
     * @return true if the slot is successfully added
     */
    public Boolean addSlot(Slot slot){
        slotsDAO.addSlot(slot);
        return true;
    }

    /**
     * Removes a gym center by its ID.
     * 
     * @param centreID ID of the gym center to be removed
     * @return true if the gym center is successfully removed
     */
    public Boolean RemoveCentre(Long centreID){
        return true;
    }

    /**
     * Removes a specific slot from a gym center.
     * 
     * @param slot Slot object to be removed
     * @param centreID ID of the gym center
     * @return true if the slot is successfully removed
     */
    public Boolean RemoveSlot(Slot slot, Long centreID){
        return true;
    }

    /**
     * Retrieves all bookings for a specific gym center.
     * 
     * @param centreID ID of the gym center
     * @return List of BookSlot objects associated with the gym center
     */
    public List<BookSlot> ViewBookings(Long centreID){
        return null;
    }
    
    /**
     * Validates the gym owner's login credentials.
     * 
     * @param email Gym owner's email address
     * @param password Gym owner's password
     * @return true if the credentials are valid, false otherwise
     */
    public boolean validUser(String email, String password){
        GymOwner gymOwner = ownerDAO.getGymOwnerByEmail(email);
        if(Objects.isNull(gymOwner) || Objects.isNull(gymOwner.getPassword()) || !Objects.equals(gymOwner.getPassword(), password)){
            return false;
        }
        return true;
    }
    
    /**
     * Creates a new gym owner with the provided details.
     * 
     * @param ownerName Name of the gym owner
     * @param ownerEmailAddress Email address of the gym owner
     * @param ownerPassword Password for the gym owner account
     * @param ownerPhone Phone number of the gym owner
     * @param ownerPanNum PAN number of the gym owner
     * @param isApproved Approval status of the gym owner
     * @param ownerAddress Address of the gym owner
     * @return Created GymOwner object
     */
    public GymOwner createGymOwner(
     String ownerName,
     String ownerEmailAddress,
     String ownerPassword,
     String ownerPhone,
     String ownerPanNum,
     boolean isApproved,
     String ownerAddress) {
    	GymOwner gymOwner= new GymOwner();
    	gymOwner.setApproved(isApproved);
    	gymOwner.setOwnerAddress(ownerAddress);
    	gymOwner.setOwnerEmailAddress(ownerEmailAddress);
    	gymOwner.setOwnerName(ownerName);
        gymOwner.setPassword(ownerPassword);
    	gymOwner.setOwnerPanNum(ownerPanNum);
    	gymOwner.setOwnerPhone(ownerPhone);

        ownerDAO.addOwner(gymOwner);
    	return gymOwner;
    }
    
    /**
     * Retrieves a gym owner by email address.
     * 
     * @param email Email address of the gym owner
     * @return GymOwner object if found, null otherwise
     */
    public GymOwner getGymOwnerByEmail(String email){
    	return ownerDAO.getGymOwnerByEmail(email);
    }
}
