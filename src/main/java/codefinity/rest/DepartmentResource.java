package codefinity.rest;

import codefinity.model.Department;
import codefinity.service.DepartmentService;
import codefinity.service.impl.DepartmentServiceImpl;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/api/departments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DepartmentResource {
    private final DepartmentService service = new DepartmentServiceImpl();

    @GET
    public List<DepartmentResponse> getAll() {
        return service.getAll().stream().map(DepartmentResponse::from).toList();
    }

    @GET
    @Path("/{id}")
    public DepartmentResponse getById(@PathParam("id") int id) {
        return DepartmentResponse.from(service.getById(id));
    }

    @POST
    public Response create(DepartmentRequest request) {
        Department department = request.toEntity();
        service.add(department);
        return Response.created(URI.create("/api/departments/" + department.getId()))
                .entity(DepartmentResponse.from(department)).build();
    }

    @PUT
    @Path("/{id}")
    public DepartmentResponse update(@PathParam("id") int id, DepartmentRequest request) {
        return DepartmentResponse.from(service.updateDepartment(id, request.toEntity()));
    }

    @PUT
    @Path("/{id}/location")
    public DepartmentResponse updateLocation(@PathParam("id") int id, LocationRequest request) {
        return DepartmentResponse.from(service.updateDepartmentLocation(id, request.location()));
    }

    public record DepartmentRequest(String name, String location) {
        Department toEntity() {
            Department department = new Department();
            department.setName(name);
            department.setLocation(location);
            return department;
        }
    }

    public record LocationRequest(String location) { }

    public record DepartmentResponse(int id, String name, String location) {
        static DepartmentResponse from(Department department) {
            return new DepartmentResponse(department.getId(), department.getName(), department.getLocation());
        }
    }
}
