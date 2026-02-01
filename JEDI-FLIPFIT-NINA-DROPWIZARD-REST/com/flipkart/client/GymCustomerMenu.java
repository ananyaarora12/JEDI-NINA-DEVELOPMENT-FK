package com.flipkart.client;

import com.flipkart.DAO.GymCustomerDAO;
import com.flipkart.DAO.GymCustomerDAOInterface;
import com.flipkart.bean.BookSlot;
import com.flipkart.bean.Customer;
import com.flipkart.bean.GymCenter;
import com.flipkart.bean.Slot;
import com.flipkart.business.CustomerOperations;
import com.flipkart.exceptions.UserNotFoundException;
import com.flipkart.utils.BookingStatusType;

import java.sql.Date;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * GymCustomerMenu - Customer REST controller
 */
@Path("/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GymCustomerMenu {

    private final CustomerOperations customerOperation = new CustomerOperations();
    private final GymCustomerDAOInterface gymCustomerDAO = new GymCustomerDAO();

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        if (request == null || request.email == null || request.password == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Email and password are required."))
                .build();
        }
        try {
            if (!customerOperation.validUser(request.email, request.password)) {
                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiResponse.failure("Invalid email or password."))
                    .build();
            }
            Customer customer = gymCustomerDAO.getCustomerByEmail(request.email);
            if (customer != null) {
                customer.setPassword(null);
            }
            return Response.ok(ApiResponse.success("Login successful.", customer)).build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(ApiResponse.failure(e.getMessage()))
                .build();
        }
    }

    @POST
    @Path("/register")
    public Response register(CustomerRegistrationRequest request) {
        if (request == null || request.name == null || request.email == null
            || request.password == null || request.phone == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Name, email, password, and phone are required."))
                .build();
        }
        Customer customer = customerOperation.createCustomer(
            request.name,
            request.email,
            request.phone,
            request.password,
            request.city,
            request.address
        );
        if (customer != null) {
            customer.setPassword(null);
        }
        return Response.ok(ApiResponse.success("Registration successful.", customer)).build();
    }

    @GET
    @Path("/centers")
    public Response viewAllGymCenters() {
        List<GymCenter> gymCenters = customerOperation.viewAllGymCenters();
        return Response.ok(ApiResponse.success("Gym centers fetched.", gymCenters)).build();
    }

    @GET
    @Path("/centers/by-city")
    public Response viewGymCentersByCity(@QueryParam("city") String city) {
        if (city == null || city.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("City is required."))
                .build();
        }
        List<GymCenter> gymCenters = customerOperation.getGymCentersByCity(city);
        return Response.ok(ApiResponse.success("Gym centers fetched.", gymCenters)).build();
    }

    @GET
    @Path("/centers/{centerId}/slots")
    public Response viewSlotsForCenter(@PathParam("centerId") Long centerId,
                                       @QueryParam("date") String date) {
        if (centerId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Center ID is required."))
                .build();
        }
        Date slotDate = parseDate(date);
        if (hasInvalidDate(date, slotDate)) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Invalid date format. Use YYYY-MM-DD."))
                .build();
        }
        List<Slot> slots = (slotDate == null)
            ? customerOperation.getAllSlotsByCenter(centerId)
            : customerOperation.getSlotsByCenterAndDate(centerId, slotDate);
        return Response.ok(ApiResponse.success("Slots fetched.", slots)).build();
    }

    @POST
    @Path("/{customerId}/bookings")
    public Response bookSlot(@PathParam("customerId") Long customerId, BookingRequest request) {
        if (customerId == null || request == null || request.slotId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Customer ID and slot ID are required."))
                .build();
        }
        Date slotDate = parseDate(request.slotDate);
        if (hasInvalidDate(request.slotDate, slotDate)) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Invalid date format. Use YYYY-MM-DD."))
                .build();
        }
        BookingStatusType result = customerOperation.bookSlot(customerId, request.slotId, slotDate);
        return Response.ok(ApiResponse.success("Booking processed.", result)).build();
    }

    @GET
    @Path("/{customerId}/bookings")
    public Response viewMyBookings(@PathParam("customerId") Long customerId,
                                   @QueryParam("date") String date) {
        if (customerId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Customer ID is required."))
                .build();
        }
        Date slotDate = parseDate(date);
        if (hasInvalidDate(date, slotDate)) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Invalid date format. Use YYYY-MM-DD."))
                .build();
        }
        List<BookSlot> bookings = (slotDate == null)
            ? customerOperation.viewAllBooking(customerId)
            : customerOperation.viewBookingsByDate(customerId, slotDate);
        return Response.ok(ApiResponse.success("Bookings fetched.", bookings)).build();
    }

    @DELETE
    @Path("/{customerId}/bookings/{bookingId}")
    public Response cancelBooking(@PathParam("customerId") Long customerId,
                                  @PathParam("bookingId") Long bookingId) {
        if (customerId == null || bookingId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Customer ID and booking ID are required."))
                .build();
        }
        customerOperation.cancelBooking(bookingId);
        return Response.ok(ApiResponse.success("Booking cancelled successfully.", null)).build();
    }

    private Date parseDate(String dateInput) {
        if (dateInput == null || dateInput.trim().isEmpty()) {
            return null;
        }
        try {
            return Date.valueOf(dateInput.trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private boolean hasInvalidDate(String rawInput, Date parsedDate) {
        return rawInput != null && !rawInput.trim().isEmpty() && parsedDate == null;
    }

    public static class LoginRequest {
        public String email;
        public String password;
    }

    public static class CustomerRegistrationRequest {
        public String name;
        public String email;
        public String password;
        public String phone;
        public String city;
        public String address;
    }

    public static class BookingRequest {
        public Long slotId;
        public String slotDate;
    }
}
