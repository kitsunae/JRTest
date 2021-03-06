package net.example.root.service;

import net.example.root.domain.Task;

import java.util.List;

public interface TaskService {

    List<Task> getTasks(int page, int size, long userId);
    Long getNumberOfAllTasksByUser(long userId);
    Task getTask(long id);
    List<Task> getDoneTasks(int page, int size, long userId, boolean isDone);
    Task save(Task task, long userId);
    Task remove(long id);
    Task edit(Task task, long id);
    List<Task> getAllTasks(Long userId);
    List<Task> getAllDoneTasks(long userId, boolean isDone);
}
