package net.example.root.service;

import net.example.root.dao.TaskRepository;
import net.example.root.dao.UserRepository;
import net.example.root.domain.User;
import net.example.web.exceptions.LoginNotAvailableException;
import net.example.web.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository repository;
    private TaskRepository taskRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository, TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    @Override
    public User getUser(long id) {
        return repository.findOne(id);
    }

    @Override
    public List<User> getUsers(int page, int size) {
        return repository.findAll(new PageRequest(page, size)).getContent();
    }

    @Override
    public User save(User user) {
        User savedUser = repository.findByLogin(user.getLogin());
        if (savedUser!=null){
            throw new LoginNotAvailableException();
        }
        return repository.save(user);
    }

    @Override
    public User remove(long id) {
        User user = repository.findOne(id);
        if (user!=null)
            repository.delete(id);
        return user;
    }

    @Override
    public Long getNumberOfAllUsers() {
        return repository.count();
    }

    @Override
    public User getUserByLogin(String login) {
        return repository.findByLogin(login);
    }

    @Override
    public User edit(User user, long id) {
        User u = repository.findOne(id);
        if (u==null)
            throw new UserNotFoundException();
        if (user.getPassword()==null)
            user.setPassword(u.getPassword());
        user.setTasks(taskRepository.findByUserId(id));
        user.setId(id);
        return repository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public List<User> findUsers(String searchLine) {
        List<User> result = new ArrayList<>();
        List<User> users = repository.findAll();
        for (User user: users){
            if (user.getName().contains(searchLine) || searchLine.contains(user.getName()))
                result.add(user);
            else if (user.getSurname().contains(searchLine) || searchLine.contains(user.getSurname()))
                result.add(user);
            else if (user.getLogin().contains(searchLine) || searchLine.contains(user.getLogin()))
                result.add(user);
        }
        return result;
    }
}
