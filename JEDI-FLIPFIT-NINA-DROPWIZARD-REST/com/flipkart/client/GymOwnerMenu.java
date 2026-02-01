package com.flipkart.client;

import com.flipkart.DAO.SlotsDAO;
import com.flipkart.DAO.SlotsDAOInterface;
import com.flipkart.bean.GymCenter;
import com.flipkart.bean.GymOwner;
import com.flipkart.bean.Slot;
import com.flipkart.business.GymCentreOperation;
import com.flipkart.business.GymOwnerOperation;

import java.sql.Date;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * GymOwnerMenu - Gym Owner REST controller
 */
@Path("/owners")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GymOwnerMenu {

    private final GymOwnerOperation gymOwnerOperations = new GymOwnerOperation();
    private final GymCentreOperation gymCentreOperation = new GymCentreOperation();
    private final SlotsDAOInterface slotsDAO = new SlotsDAO();

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        if (request == null || request.email == null || request.password == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Email and password are required."))
                .build();
        }
        if (!gymOwnerOperations.validUser(request.email, request.password)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ApiResponse.failure("Invalid email or password."))
                .build();
        }
        GymOwner gymOwner = gymOwnerOperations.getGymOwnerByEmail(request.email);
        if (gymOwner != null) {
            gymOwner.setPassword(null);
        }
        return Response.ok(ApiResponse.success("Login successful.", gymOwner)).build();
    }

    @POST
    @Path("/register")
    public Response register(GymOwnerRegistrationRequest request) {
        if (request == null || request.name == null || request.email == null
            || request.password == null || request.phone == null
            || request.panNumber == null || request.address == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("All registration fields are required."))
                .build();
        }
        GymOwner owner = gymOwnerOperations.createGymOwner(
            request.name,
            request.email,
            request.password,
            request.phone,
            request.panNumber,
            false,
            request.address
        );
        if (owner != null) {
            owner.setPassword(null);
        }
        return Response.ok(ApiResponse.success("Registration successful. Awaiting approval.", owner)).build();
    }

    @GET
    @Path("/{ownerId}/centers")
    public Response viewMyGymCenters(@PathParam("ownerId") Long ownerId) {
        if (ownerId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Owner ID is required."))
                .build();
        }
        List<GymCenter> centers = gymCentreOperation.getAllGymCentersByGymOwnerId(ownerId);
        return Response.ok(ApiResponse.success("Gym centers fetched.", centers)).build();
    }

    @POST
    @Path("/{ownerId}/centers")
    public Response addNewGymCenter(@PathParam("ownerId") Long ownerId, GymCenterRequest request) {
        if (ownerId == null || request == null || request.name == null
            || request.city == null || request.location == null || request.capacity == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Name, city, location, and capacity are required."))
                .build();
        }
        GymCenter gymCenter = new GymCenter();
        gymCenter.setName(request.name);
        gymCenter.setCity(request.city);
        gymCenter.setLocation(request.location);
        gymCenter.setCapacity(request.capacity);
        gymCenter.setStatus("PENDING");
        gymCenter.setGymOwnerId(ownerId);

        gymOwnerOperations.addCentre(gymCenter);
        return Response.ok(ApiResponse.success("Gym center submitted for approval.", gymCenter)).build();
    }

    @GET
    @Path("/centers/{centerId}/slots")
    public Response viewSlotsForCenter(@PathParam("centerId") Long centerId) {
        if (centerId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Center ID is required."))
                .build();
        }
        List<Slot> slots = slotsDAO.getAllSlotsByGymCenterId(centerId);
        return Response.ok(ApiResponse.success("Slots fetched.", slots)).build();
    }

    @POST
    @Path("/centers/{centerId}/slots")
    public Response addSlotToCenter(@PathParam("centerId") Long centerId, SlotRequest request) {
        if (centerId == null || request == null || request.date == null
            || request.startTime == null || request.endTime == null
            || request.totalSeats == null || request.price == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Date, startTime, endTime, totalSeats, and price are required."))
                .build();
        }
        Date date = parseDate(request.date);
        if (date == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Invalid date format. Use YYYY-MM-DD."))
                .build();
        }
        Slot slot = new Slot();
        slot.setCentreId(centerId);
        slot.setDate(date);
        slot.setStartTime(request.startTime);
        slot.setEndTime(request.endTime);
        slot.setTotalSeats(request.totalSeats);
        slot.setAvailableSeats(request.totalSeats);
        slot.setPrice(request.price);

        slotsDAO.addSlot(slot);
        return Response.ok(ApiResponse.success("Slot added successfully.", slot)).build();
    }

    @DELETE
    @Path("/slots/{slotId}")
    public Response removeSlot(@PathParam("slotId") Long slotId) {
        if (slotId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Slot ID is required."))
                .build();
        }
        slotsDAO.deleteSlot(slotId);
        return Response.ok(ApiResponse.success("Slot removed successfully.", null)).build();
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

    public static class LoginRequest {
        public String email;
        public String password;
    }

    public static class GymOwnerRegistrationRequest {
        public String name;
        public String email;
        public String password;
        public String phone;
        public String panNumber;
        public String address;
    }

    public static class GymCenterRequest {
        public String name;
        public String city;
        public String location;
        public Integer capacity;
    }

    public static class SlotRequest {
        public String date;
        public String startTime;
        public String endTime;
        public Integer totalSeats;
        public Integer price;
    }
}
