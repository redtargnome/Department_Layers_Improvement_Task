package codefinity.service.impl;

import codefinity.dao.TaskDao;
import codefinity.dao.impl.TaskDaoImpl;
import codefinity.enums.TaskStatus;
import codefinity.model.Employee;
import codefinity.model.Task;
import codefinity.service.TaskService;

import java.util.List;
import java.util.Objects;

public class TaskServiceImpl implements TaskService {
    private final TaskDao taskDao = new TaskDaoImpl();
    @Override
    public Task add(Task task) {
        return taskDao.add(task);
    }

    @Override
    public Task getById(int id) {
        return taskDao.getById(id);
    }

    @Override
    public List<Task> getAll() {
        return taskDao.getAll();
    }

    @Override
    public Task updateTask(int id, Task task) {
        return taskDao.updateTask(id, task);
    }

    @Override
    public Task updateAnEmployeeToTheTask(Task task, Employee employee) {
        Objects.requireNonNull(task, "Task is null");
        Objects.requireNonNull(employee, "Employee is null");
        task.setEmployee(employee);
        return updateTask(task.getId(), task);
    }

    @Override
    public Task updateTaskStatus(Task task, TaskStatus status) {
        Objects.requireNonNull(task, "Task is null");
        Objects.requireNonNull(status, "Task status is null");
        task.setStatus(status);
        return updateTask(task.getId(), task);
    }
}
