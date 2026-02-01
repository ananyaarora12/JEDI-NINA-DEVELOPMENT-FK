package com.flipkart.client;

import com.flipkart.bean.GymAdmin;
import com.flipkart.bean.GymCenter;
import com.flipkart.bean.GymOwner;
import com.flipkart.business.AdminOperation;
import com.flipkart.exceptions.UserNotFoundException;

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * GymAdminMenu - Admin REST controller
 */
@Path("/admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GymAdminMenu {

    private final AdminOperation adminOperation = new AdminOperation();

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        if (request == null || request.email == null || request.password == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Email and password are required."))
                .build();
        }
        try {
            if (!adminOperation.validUser(request.email, request.password)) {
                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiResponse.failure("Invalid email or password."))
                    .build();
            }
            GymAdmin admin = adminOperation.getAdminByEmail(request.email);
            if (admin != null) {
                admin.setPassword(null);
            }
            return Response.ok(ApiResponse.success("Login successful.", admin)).build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(ApiResponse.failure(e.getMessage()))
                .build();
        }
    }

    @POST
    @Path("/register")
    public Response register(AdminRegistrationRequest request) {
        if (request == null || request.name == null || request.email == null
            || request.password == null || request.phone == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Name, email, password, and phone are required."))
                .build();
        }
        GymAdmin admin = adminOperation.createAdmin(
            request.name,
            request.email,
            request.phone,
            request.password
        );
        if (admin != null) {
            admin.setPassword(null);
        }
        return Response.ok(ApiResponse.success("Admin registered successfully.", admin)).build();
    }

    @GET
    @Path("/owners/pending")
    public Response viewPendingGymOwners() {
        List<GymOwner> owners = adminOperation.viewPendingGymOwners();
        sanitizeOwners(owners);
        return Response.ok(ApiResponse.success("Pending gym owners fetched.", owners)).build();
    }

    @GET
    @Path("/owners/approved")
    public Response viewApprovedGymOwners() {
        List<GymOwner> owners = adminOperation.viewApprovedGymOwners();
        sanitizeOwners(owners);
        return Response.ok(ApiResponse.success("Approved gym owners fetched.", owners)).build();
    }

    @GET
    @Path("/owners/filter")
    public Response filterGymOwners(@QueryParam("approved") boolean approved) {
        List<GymOwner> owners = adminOperation.filterGymOwnersByApproval(approved);
        sanitizeOwners(owners);
        return Response.ok(ApiResponse.success("Gym owners filtered.", owners)).build();
    }

    @POST
    @Path("/owners/{ownerId}/approve")
    public Response approveGymOwner(@PathParam("ownerId") long ownerId) {
        boolean result = adminOperation.approveGymOwner(ownerId);
        if (!result) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Failed to approve gym owner."))
                .build();
        }
        return Response.ok(ApiResponse.success("Gym owner approved successfully.", null)).build();
    }

    @POST
    @Path("/owners/{ownerId}/reject")
    public Response rejectGymOwner(@PathParam("ownerId") long ownerId) {
        boolean result = adminOperation.rejectGymOwner(ownerId);
        if (!result) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Failed to reject gym owner."))
                .build();
        }
        return Response.ok(ApiResponse.success("Gym owner rejected successfully.", null)).build();
    }

    @GET
    @Path("/centers/pending")
    public Response viewPendingCenters() {
        List<GymCenter> centers = adminOperation.viewPendingGymCentres();
        return Response.ok(ApiResponse.success("Pending gym centers fetched.", centers)).build();
    }

    @GET
    @Path("/centers/approved")
    public Response viewApprovedCenters() {
        List<GymCenter> centers = adminOperation.viewApprovedGymCentres();
        return Response.ok(ApiResponse.success("Approved gym centers fetched.", centers)).build();
    }

    @GET
    @Path("/centers/filter")
    public Response filterGymCenters(@QueryParam("approved") boolean approved) {
        List<GymCenter> centers = adminOperation.filterGymCentersByApproval(approved);
        return Response.ok(ApiResponse.success("Gym centers filtered.", centers)).build();
    }

    @POST
    @Path("/centers/{centerId}/approve")
    public Response approveGymCenter(@PathParam("centerId") long centerId) {
        boolean result = adminOperation.approveGymCenter(centerId);
        if (!result) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Failed to approve gym center."))
                .build();
        }
        return Response.ok(ApiResponse.success("Gym center approved successfully.", null)).build();
    }

    @POST
    @Path("/centers/{centerId}/reject")
    public Response rejectGymCenter(@PathParam("centerId") long centerId) {
        boolean result = adminOperation.rejectGymCenter(centerId);
        if (!result) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failure("Failed to reject gym center."))
                .build();
        }
        return Response.ok(ApiResponse.success("Gym center rejected successfully.", null)).build();
    }

    private void sanitizeOwners(List<GymOwner> owners) {
        if (owners == null) {
            return;
        }
        for (GymOwner owner : owners) {
            if (owner != null) {
                owner.setPassword(null);
            }
        }
    }

    public static class LoginRequest {
        public String email;
        public String password;
    }

    public static class AdminRegistrationRequest {
        public String name;
        public String email;
        public String password;
        public String phone;
    }
}
