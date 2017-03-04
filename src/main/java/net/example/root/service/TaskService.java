package net.example.root.service;

import net.example.root.domain.Task;

import java.util.List;

/**
 * Created by lashi on 24.02.2017.
 */
public interface TaskService {

    List<Task> getTasks(int page, int size, long userId);
    Long getNumberOfAllTasksByUser(long userId);
    Task getTask(long id);
    List<Task> getDoneTasks(int page, int size, long userId, boolean isDone);
    Task save(Task task, long userId);
    Task remove(long id);
    Task edit(Task task, long id);
}
