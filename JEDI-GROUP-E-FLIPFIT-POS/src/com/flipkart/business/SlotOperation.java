package com.flipkart.business;

import com.flipkart.bean.Slot;

public class SlotOperation {

    // Method to get a Slot by its ID
    public Slot getSlot(Long slotId) {
        // Print a message indicating that we are getting the slot with the given ID
        System.out.println("Getting slot with Slot Id: " + slotId);
        // Return a new Slot object (this would likely be replaced with logic to fetch a real slot)
        return new Slot();
    }

    // Method to add a new Slot
    public Slot addSlot(Slot slot) {
        // Print a message indicating that we are adding the provided slot
        System.out.println("Adding slot: " + slot);
        // Return the added slot (in a real system, this might involve saving to a database)
        return slot;
    }

    // Method to remove a Slot by its ID
    public Slot removeSlot(Long slotId) {
        // Print a message indicating that we are removing the slot with the given ID
        System.out.println("Removing slot with Slot Id: " + slotId);
        // Return a new Slot object (this would typically involve removing the slot from a database or list)
        return new Slot();
    }

    // Method to book a Slot by its ID
    public boolean bookSlot(Long slotId) {
        // Print a message indicating that we are booking the slot with the given ID
        System.out.println("Booking slot with Slot Id: " + slotId);
        // Return true to indicate that the booking was successful (in a real system, this would involve more logic)
        return true;
    }
}

