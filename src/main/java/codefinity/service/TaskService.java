package codefinity.service;

import codefinity.enums.TaskStatus;
import codefinity.model.Employee;
import codefinity.model.Task;

import java.util.List;

public interface TaskService {

    Task add(Task task);

    Task getById(int id);

    List<Task> getAll();

    Task updateTask(int id, Task task);

    Task updateAnEmployeeToTheTask(Task task, Employee employee);

    Task updateTaskStatus(Task task, TaskStatus status);
}
