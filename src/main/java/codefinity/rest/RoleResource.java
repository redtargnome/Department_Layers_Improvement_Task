package codefinity.rest;

import codefinity.model.Role;
import codefinity.service.RoleService;
import codefinity.service.impl.RoleServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/api/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoleResource {
    private final RoleService service = new RoleServiceImpl();

    @GET
    public List<RoleResponse> getAll() {
        return service.getAll().stream().map(RoleResponse::from).toList();
    }

    @GET
    @Path("/{id}")
    public RoleResponse getById(@PathParam("id") int id) {
        return RoleResponse.from(service.getById(id));
    }

    @POST
    public Response create(RoleRequest request) {
        Role role = request.toEntity();
        service.add(role);
        return Response.created(URI.create("/api/roles/" + role.getId())).entity(RoleResponse.from(role)).build();
    }

    @PUT
    @Path("/{id}")
    public RoleResponse update(@PathParam("id") int id, RoleRequest request) {
        return RoleResponse.from(service.updateRole(id, request.toEntity()));
    }

    public record RoleRequest(String name, String description) {
        Role toEntity() {
            Role role = new Role();
            role.setName(name);
            role.setDescription(description);
            return role;
        }
    }

    public record RoleResponse(int id, String name, String description) {
        static RoleResponse from(Role role) {
            return new RoleResponse(role.getId(), role.getName(), role.getDescription());
        }
    }
}
