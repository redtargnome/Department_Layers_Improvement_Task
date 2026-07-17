package codefinity.rest;

import codefinity.model.Department;
import codefinity.model.Employee;
import codefinity.model.Role;
import codefinity.service.DepartmentService;
import codefinity.service.EmployeeService;
import codefinity.service.RoleService;
import codefinity.service.impl.DepartmentServiceImpl;
import codefinity.service.impl.EmployeeServiceImpl;
import codefinity.service.impl.RoleServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Path("/api/employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeResource {
    private final EmployeeService service = new EmployeeServiceImpl();
    private final DepartmentService departments = new DepartmentServiceImpl();
    private final RoleService roles = new RoleServiceImpl();

    @GET
    public List<EmployeeResponse> getAll(@QueryParam("minimumSalary") Double minimumSalary,
                                         @QueryParam("hiredFrom") String hiredFrom,
                                         @QueryParam("hiredTo") String hiredTo) {
        List<Employee> employees;
        if (minimumSalary != null) {
            employees = service.getEmployeesWithSalaryMoreThan(minimumSalary);
        } else if (hiredFrom != null || hiredTo != null) {
            if (hiredFrom == null || hiredTo == null) {
                throw new BadRequestException("Both hiredFrom and hiredTo are required");
            }
            employees = service.getEmployeesHiredInASpecificTimeframe(hiredFrom, hiredTo);
        } else {
            employees = service.getAll();
        }
        return employees.stream().map(EmployeeResponse::from).toList();
    }

    @GET
    @Path("/{id}")
    public EmployeeResponse getById(@PathParam("id") int id) {
        return EmployeeResponse.from(service.getById(id));
    }

    @POST
    public Response create(EmployeeRequest request) {
        Employee employee = request.toEntity(departments, roles);
        service.add(employee);
        return Response.created(URI.create("/api/employees/" + employee.getId()))
                .entity(EmployeeResponse.from(employee)).build();
    }

    @PUT
    @Path("/{id}")
    public EmployeeResponse update(@PathParam("id") int id, EmployeeRequest request) {
        return EmployeeResponse.from(service.updateEmployee(id, request.toEntity(departments, roles)));
    }

    @PUT
    @Path("/{id}/department/{departmentId}")
    public EmployeeResponse setDepartment(@PathParam("id") int id, @PathParam("departmentId") int departmentId) {
        return EmployeeResponse.from(service.setDepartmentById(id, departmentId));
    }

    @PUT
    @Path("/{id}/role/{roleId}")
    public EmployeeResponse setRole(@PathParam("id") int id, @PathParam("roleId") int roleId) {
        return EmployeeResponse.from(service.setRoleById(id, roleId));
    }

    public record EmployeeRequest(String name, double salary, LocalDate hireDate,
                                  Integer departmentId, Integer roleId) {
        Employee toEntity(DepartmentService departments, RoleService roles) {
            Employee employee = new Employee();
            employee.setName(name);
            employee.setSalary(salary);
            employee.setHireDate(hireDate);
            if (departmentId != null) employee.setDepartment(departments.getById(departmentId));
            if (roleId != null) employee.setRole(roles.getById(roleId));
            return employee;
        }
    }

    public record EmployeeResponse(int id, String name, double salary, LocalDate hireDate,
                                   Integer departmentId, Integer roleId) {
        static EmployeeResponse from(Employee employee) {
            Department department = employee.getDepartment();
            Role role = employee.getRole();
            return new EmployeeResponse(employee.getId(), employee.getName(), employee.getSalary(),
                    employee.getHireDate(), department == null ? null : department.getId(),
                    role == null ? null : role.getId());
        }
    }
}
