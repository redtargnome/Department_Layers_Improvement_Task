package codefinity.rest;

import codefinity.enums.TaskStatus;
import codefinity.model.Employee;
import codefinity.model.Task;
import codefinity.service.EmployeeService;
import codefinity.service.TaskService;
import codefinity.service.impl.EmployeeServiceImpl;
import codefinity.service.impl.TaskServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Path("/api/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {
    private final TaskService service = new TaskServiceImpl();
    private final EmployeeService employees = new EmployeeServiceImpl();

    @GET
    public List<TaskResponse> getAll() {
        return service.getAll().stream().map(TaskResponse::from).toList();
    }

    @GET
    @Path("/{id}")
    public TaskResponse getById(@PathParam("id") int id) {
        Task task = service.getById(id);
        if (task == null) throw new NotFoundException("Task " + id + " was not found");
        return TaskResponse.from(task);
    }

    @POST
    public Response create(TaskRequest request) {
        Task task = request.toEntity(employees);
        service.add(task);
        return Response.created(URI.create("/api/tasks/" + task.getId())).entity(TaskResponse.from(task)).build();
    }

    @PUT
    @Path("/{id}")
    public TaskResponse update(@PathParam("id") int id, TaskRequest request) {
        return TaskResponse.from(service.updateTask(id, request.toEntity(employees)));
    }

    @PUT
    @Path("/{id}/status")
    public TaskResponse updateStatus(@PathParam("id") int id, StatusRequest request) {
        Task task = service.getById(id);
        if (task == null) throw new NotFoundException("Task " + id + " was not found");
        return TaskResponse.from(service.updateTaskStatus(task, request.status()));
    }

    @PUT
    @Path("/{id}/employee/{employeeId}")
    public TaskResponse setEmployee(@PathParam("id") int id, @PathParam("employeeId") int employeeId) {
        Task task = service.getById(id);
        if (task == null) throw new NotFoundException("Task " + id + " was not found");
        return TaskResponse.from(service.updateAnEmployeeToTheTask(task, employees.getById(employeeId)));
    }

    public record TaskRequest(String title, String description, LocalDate deadline,
                              TaskStatus status, Integer employeeId) {
        Task toEntity(EmployeeService employees) {
            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setDeadline(deadline);
            task.setStatus(status);
            if (employeeId != null) task.setEmployee(employees.getById(employeeId));
            return task;
        }
    }

    public record StatusRequest(TaskStatus status) { }

    public record TaskResponse(int id, String title, String description, LocalDate deadline,
                               TaskStatus status, Integer employeeId) {
        static TaskResponse from(Task task) {
            Employee employee = task.getEmployee();
            return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getDeadline(),
                    task.getStatus(), employee == null ? null : employee.getId());
        }
    }
}
