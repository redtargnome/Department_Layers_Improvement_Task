package codefinity;

import codefinity.enums.TaskStatus;
import codefinity.model.Department;
import codefinity.model.Employee;
import codefinity.model.Role;
import codefinity.model.Task;
import codefinity.service.DepartmentService;
import codefinity.service.EmployeeService;
import codefinity.service.RoleService;
import codefinity.service.TaskService;
import codefinity.service.impl.DepartmentServiceImpl;
import codefinity.service.impl.EmployeeServiceImpl;
import codefinity.service.impl.RoleServiceImpl;
import codefinity.service.impl.TaskServiceImpl;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        EmployeeService employeeService = new EmployeeServiceImpl();
        DepartmentService departmentService = new DepartmentServiceImpl();
        RoleService roleService = new RoleServiceImpl();
        TaskService taskService = new TaskServiceImpl();

        Department marketing = new Department();
        marketing.setLocation("Head Office");
        marketing.setName("Marketing");
        departmentService.add(marketing);

        Department it = new Department();
        it.setName("Development");
        it.setLocation("IT Office");
        departmentService.add(it);

        Role marketer = new Role();
        marketer.setName("Marketing Specialist");
        marketer.setDescription("A dynamic role focused on developing and implementing strategies to promote brand " +
                "awareness and drive product sales. Responsibilities include market research, content creation, " +
                "and campaign management to enhance engagement and achieve business goals.");
        roleService.add(marketer);

        Role developer = new Role();
        developer.setName("Software Engineer");
        developer.setDescription("A software engineer writes and maintains " +
                "the code for computer applications and systems.");
        roleService.add(developer);

        Employee john = new Employee();
        john.setName("John");
        john.setSalary(55000.00);
        john.setHireDate(LocalDate.of(2021, 10, 5));
        john.setDepartment(marketing);
        employeeService.add(john);

        employeeService.setRoleById(john.getId(), marketer.getId());

        Employee bob = new Employee();
        bob.setName("Bob");
        bob.setSalary(70000.00);
        bob.setDepartment(it);
        bob.setHireDate(LocalDate.of(2022, 2, 18));

        Role bobsRole = roleService.getById(developer.getId());
        bob.setRole(bobsRole);

        employeeService.add(bob);
        Task onBoard = new Task();
        onBoard.setTitle("Onboarding");
        onBoard.setDescription("Participate in onboarding");
        onBoard.setDeadline(LocalDate.now());
        onBoard.setStatus(TaskStatus.STARTED);
        onBoard.setEmployee(bob);
        taskService.add(onBoard);


        System.out.println(taskService.getAll());

        taskService.updateTaskStatus(onBoard,TaskStatus.IN_PROCESS);

        System.out.println(taskService.getAll());

        taskService.updateAnEmployeeToTheTask(onBoard, john);

        System.out.println(taskService.getAll());
        System.out.println("All departments info: " + departmentService.getAll());

        System.out.println("Marketing department before changes: " + departmentService.getById(marketing.getId()));

        departmentService.updateDepartmentLocation(marketing.getId(), "Marketing Office");
         it.setName("Technology");
        System.out.println("Marketing department after changes: " + departmentService.getById(marketing.getId()));

        departmentService.updateDepartment(it.getId(),it);

        System.out.println(departmentService.getAll());
    }
}
