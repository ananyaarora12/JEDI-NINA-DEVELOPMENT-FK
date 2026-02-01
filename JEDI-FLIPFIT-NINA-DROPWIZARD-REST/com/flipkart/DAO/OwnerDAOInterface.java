package com.flipkart.DAO;
import com.flipkart.bean.GymCenter;
import com.flipkart.bean.GymOwner;
import com.flipkart.bean.Slot;

import java.util.ArrayList;

public interface OwnerDAOInterface {
    void addOwner(GymOwner gymOwner);
    ArrayList<GymOwner> getAllOwners();
    GymOwner getOwnerByID(Long gymOwnerID);
    void addCenter(GymCenter center);
    ArrayList<GymCenter> getAllCenters(Long ownerID);
    ArrayList<GymCenter> getAllApprovedCenters(Long ownerID);
    void addSlot(Long ownerID, Slot slot);
    ArrayList<Slot> getAllSlots(Long ownerID, Long centerID);
    public GymOwner getGymOwnerByEmail(String email);
}
