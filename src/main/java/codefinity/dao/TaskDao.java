package codefinity.dao;

import codefinity.model.Task;

import java.util.List;

public interface TaskDao {

    Task add(Task task);

    Task getById(int id);

    List<Task> getAll();

    Task updateTask(int id, Task newTask);
}
