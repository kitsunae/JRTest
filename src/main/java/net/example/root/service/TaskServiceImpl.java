package net.example.root.service;

import net.example.root.dao.TaskRepository;
import net.example.root.dao.UserRepository;
import net.example.root.domain.Task;
import net.example.root.domain.User;
import net.example.web.exceptions.TaskNotFoundException;
import net.example.web.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lashi on 24.02.2017.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getTasks(int page, int size, long userId) {
        return taskRepository.findByUserId(userId, new PageRequest(page, size));
    }

    @Override
    public List<Task> getAllTasks(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    public Long getNumberOfAllTasksByUser(long userId) {
        return taskRepository.countByUserId(userId);
    }

    @Override
    public Task getTask(long id) {
        return taskRepository.findOne(id);
    }

    @Override
    public List<Task> getDoneTasks(int page, int size, long userId, boolean isDone) {
        return taskRepository.findByUserIdAndDone(userId, isDone, new PageRequest(page, size));
    }

    @Override
    public List<Task> getAllDoneTasks(long userId, boolean isDone) {
        return taskRepository.findByUserIdAndDone(userId, isDone);
    }

    @Override
    public Task save(Task task, long userId) {
        User user = userRepository.findOne(userId);
        if (user==null)
            throw new UserNotFoundException();
        task.setUser(user);
        return taskRepository.save(task);
    }

    @Override
    public Task remove(long id) {
        Task task = taskRepository.findOne(id);
        if (task==null)
            throw new TaskNotFoundException();
        taskRepository.delete(id);
        return task;
    }

    @Override
    public Task edit(Task task, long id) {
        task.setId(id);
        return taskRepository.save(task);
    }
}
