package com.flipkart.business;

import com.flipkart.DAO.*;
import com.flipkart.bean.*;
import com.flipkart.exceptions.UserNotFoundException;
import com.flipkart.utils.BookingStatusType;
import com.flipkart.utils.NotificationType;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * CustomerOperations - Business logic for Customer
 * 
 * From Activity Diagram (Customer):
 * 1. "Sign up" / "Login"
 * 2. "Select City" -> Get gym centers
 * 3. "Choose Date & Time Slot"
 * 4. "Initiate Conflict Check"
 * 5. "Verify Seat Availability"
 * 6. "Create Booking Record & Decrement Seat Count" OR "Add User to Waitlist"
 * 7. "View My Plan"
 * 8. "Select Booking to Cancel" -> "Check Waitlist for Slot" -> "Promotion Notification"
 */
public class CustomerOperations {

    private GymCustomerDAOInterface customerDAO = new GymCustomerDAO();
    private BookSlotDAOInterface bookingDAO = new BookSlotDAO();
    private SlotsDAOInterface slotDAO = new SlotsDAO();
    private GymCenterDAOInterface gymCenterDAO = new GymCenterDAO();
    private WaitlistDAOInterface waitlistDAO = new WaitlistDAO();
    private NotificationDaoInterface notificationDAO = new NotificationDao();

    // ==================== AUTHENTICATION ====================
    
    public Customer createCustomer(String name, String email, String phone, String password, String city, String address) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setPassword(password);
        customer.setCity(city);
        customer.setAddress(address);
        customerDAO.addCustomer(customer);
        return customer;
    }

    public boolean validUser(String email, String password) throws UserNotFoundException {
        Customer customer = customerDAO.getCustomerByEmail(email);
        if (Objects.isNull(customer) || Objects.isNull(customer.getCustomerId())) {
            throw new UserNotFoundException(email);
        }
        return Objects.equals(customer.getPassword(), password);
    }

    // ==================== GYM CENTER SEARCH ====================
    
    public List<GymCenter> viewAllGymCenters() {
        return gymCenterDAO.getAllApprovedGymCenters();
    }
    
    public List<GymCenter> getGymCentersByCity(String city) {
        return gymCenterDAO.getApprovedGymCentersByCity(city);
    }

    // ==================== SLOT VIEWING ====================
    
    public List<Slot> getSlotsByCenterAndDate(Long centerId, Date date) {
        return slotDAO.getSlotsByCenterAndDate(centerId, date);
    }
    
    public List<Slot> getAllSlotsByCenter(Long centerId) {
        return slotDAO.getAllSlotsByGymCenterId(centerId);
    }

    // ==================== BOOKING FLOW ====================
    
    /**
     * Complete booking flow from Activity Diagram
     */
    public BookingStatusType bookSlot(Long customerId, Long slotId, Date slotDate) {
        // Step 1: Load slot and validate date
        Slot slot = slotDAO.getSlotById(slotId);
        if (slot == null) {
            System.out.println("Slot not found!");
            return BookingStatusType.CANCELLED;
        }

        Date effectiveDate = slotDate != null ? slotDate : slot.getDate();
        if (effectiveDate == null) {
            System.out.println("Slot date is missing. Please select a valid date.");
            return BookingStatusType.CANCELLED;
        }
        if (slot.getDate() != null && effectiveDate != null && !slot.getDate().equals(effectiveDate)) {
            System.out.println("Selected slot does not match the chosen date.");
            return BookingStatusType.CANCELLED;
        }

        // Step 2: Conflict Check (same time slot)
        BookSlot conflict = bookingDAO.checkBookingConflictByTime(
            customerId,
            effectiveDate,
            slot.getStartTime(),
            slot.getEndTime()
        );
        if (conflict != null) {
            if (conflict.getSlotId().equals(slotId)) {
                System.out.println("Conflict found! Already have a booking for this slot.");
                return BookingStatusType.CANCELLED;
            }
            System.out.println("Existing booking in the same time slot found. Replacing it.");
            cancelBooking(conflict.getBookingId());
        }
        
        // Step 3: Seats Available?
        if (slot.hasAvailableSeats()) {
            return confirmBooking(customerId, slotId, effectiveDate);
        } else {
            BookingStatusType status = addToWaitlist(customerId, slotId, effectiveDate);
            suggestNearestAvailableSlot(customerId, slot, effectiveDate);
            return status;
        }
    }
    
    private BookingStatusType confirmBooking(Long customerId, Long slotId, Date slotDate) {
        boolean seatDecremented = slotDAO.decrementSeatCount(slotId);
        if (!seatDecremented) {
            BookingStatusType status = addToWaitlist(customerId, slotId, slotDate);
            Slot slot = slotDAO.getSlotById(slotId);
            if (slot != null) {
                suggestNearestAvailableSlot(customerId, slot, slotDate);
            }
            return status;
        }
        
        BookSlot booking = new BookSlot();
        booking.setCustomerId(customerId);
        booking.setSlotId(slotId);
        booking.setSlotDate(slotDate);
        booking.setBookingStatus(BookingStatusType.CONFIRMED);
        bookingDAO.addBooking(booking);
        
        sendNotification(customerId, "Your booking has been confirmed!", NotificationType.BOOKING_CONFIRMED);
        System.out.println("Booking confirmed!");
        return BookingStatusType.CONFIRMED;
    }
    
    private BookingStatusType addToWaitlist(Long customerId, Long slotId, Date slotDate) {
        if (waitlistDAO.isCustomerInWaitlist(customerId, slotId)) {
            int position = waitlistDAO.getCustomerPosition(customerId, slotId);
            System.out.println("Already in waitlist at position: " + position);
            return BookingStatusType.WAITLISTED;
        }
        
        WaitingList entry = new WaitingList();
        entry.setCustomerId(customerId);
        entry.setSlotId(slotId);
        waitlistDAO.addToWaitlist(entry);
        
        BookSlot booking = new BookSlot();
        booking.setCustomerId(customerId);
        booking.setSlotId(slotId);
        booking.setSlotDate(slotDate);
        booking.setBookingStatus(BookingStatusType.WAITLISTED);
        bookingDAO.addBooking(booking);
        
        System.out.println("Added to waitlist.");
        return BookingStatusType.WAITLISTED;
    }

    // ==================== VIEW & CANCEL ====================
    
    public List<BookSlot> viewAllBooking(Long customerId) {
        return bookingDAO.getBookingsByCustomer(customerId);
    }

    public List<BookSlot> viewBookingsByDate(Long customerId, Date slotDate) {
        return bookingDAO.getBookingsByCustomerAndDate(customerId, slotDate);
    }
    
    public void cancelBooking(Long bookingId) {
        BookSlot booking = bookingDAO.getBookingById(bookingId);
        if (booking == null) return;
        
        bookingDAO.cancelBooking(bookingId);
        slotDAO.incrementSeatCount(booking.getSlotId());
        
        // Check waitlist and promote
        promoteFromWaitlist(booking.getSlotId());
        
        sendNotification(booking.getCustomerId(), "Your booking has been cancelled.", NotificationType.BOOKING_CANCELLED);
    }
    
    private void promoteFromWaitlist(Long slotId) {
        if (waitlistDAO.getWaitlistCount(slotId) > 0) {
            WaitingList first = waitlistDAO.getFirstInWaitlist(slotId);
            if (first != null) {
                List<BookSlot> bookings = bookingDAO.getBookingsByCustomer(first.getCustomerId());
                for (BookSlot b : bookings) {
                    if (b.getSlotId().equals(slotId) && b.getBookingStatus() == BookingStatusType.WAITLISTED) {
                        bookingDAO.updateBookingStatus(b.getBookingId(), BookingStatusType.CONFIRMED.toString());
                        slotDAO.decrementSeatCount(slotId);
                        waitlistDAO.removeFromWaitlist(first.getWaitlistId());
                        sendNotification(first.getCustomerId(), "You have been promoted from waitlist!", NotificationType.WAITLIST_PROMOTION);
                        break;
                    }
                }
            }
        }
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

    private void suggestNearestAvailableSlot(Long customerId, Slot desiredSlot, Date slotDate) {
        Slot nearest = findNearestAvailableSlot(customerId, desiredSlot, slotDate);
        if (nearest != null) {
            System.out.println("Nearest available slot: " + nearest.getSlotID()
                + " (" + nearest.getStartTime() + " - " + nearest.getEndTime() + ")");
        }
    }

    private Slot findNearestAvailableSlot(Long customerId, Slot desiredSlot, Date slotDate) {
        if (desiredSlot == null || slotDate == null) {
            return null;
        }
        List<Slot> slots = slotDAO.getSlotsByCenterAndDate(desiredSlot.getCentreId(), slotDate);
        if (slots == null || slots.isEmpty()) {
            return null;
        }
        List<Slot> bookedSlots = slotDAO.getBookedSlotsByCustomerAndDate(customerId, slotDate);
        int desiredStart = parseMinutes(desiredSlot.getStartTime());
        if (desiredStart < 0) {
            return null;
        }
        Slot best = null;
        int bestDelta = Integer.MAX_VALUE;
        for (Slot slot : slots) {
            if (slot.getSlotID().equals(desiredSlot.getSlotID())) {
                continue;
            }
            if (slot.getAvailableSeats() <= 0) {
                continue;
            }
            if (isOverlappingWithBooked(slot, bookedSlots)) {
                continue;
            }
            int delta = Math.abs(parseMinutes(slot.getStartTime()) - desiredStart);
            if (delta < bestDelta) {
                bestDelta = delta;
                best = slot;
            }
        }
        return best;
    }

    private boolean isOverlappingWithBooked(Slot candidate, List<Slot> bookedSlots) {
        if (candidate == null || bookedSlots == null || bookedSlots.isEmpty()) {
            return false;
        }
        int candidateStart = parseMinutes(candidate.getStartTime());
        int candidateEnd = parseMinutes(candidate.getEndTime());
        if (candidateStart < 0 || candidateEnd < 0) {
            return false;
        }
        for (Slot booked : bookedSlots) {
            int bookedStart = parseMinutes(booked.getStartTime());
            int bookedEnd = parseMinutes(booked.getEndTime());
            if (bookedStart < 0 || bookedEnd < 0) {
                continue;
            }
            if (candidateStart < bookedEnd && candidateEnd > bookedStart) {
                return true;
            }
        }
        return false;
    }

    private int parseMinutes(String time) {
        if (time == null) {
            return -1;
        }
        String[] parts = time.trim().split(":");
        if (parts.length < 2) {
            return -1;
        }
        try {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            return hours * 60 + minutes;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ==================== LEGACY METHODS ====================
    
    public Customer createCustomer(String name, String address, String email, String phone, String password) {
        return createCustomer(name, email, phone, password, null, address);
    }
    
    public void bookSlot(Long customerId, Long slotId) {
        bookSlot(customerId, slotId, new Date(System.currentTimeMillis()));
    }
    
    public void cancelBookedSlot(Long customerId, Long slotId) {
        List<BookSlot> bookings = bookingDAO.getBookingsByCustomer(customerId);
        for (BookSlot b : bookings) {
            if (b.getSlotId().equals(slotId) && b.getBookingStatus() == BookingStatusType.CONFIRMED) {
                cancelBooking(b.getBookingId());
                return;
            }
        }
    }
    
    public List<GymCenter> viewAllGymCenter() {
        return viewAllGymCenters();
    }
}
