package net.example.root.service;

import net.example.root.dao.UserRepository;
import net.example.root.domain.User;
import net.example.web.exceptions.LoginNotAvailableException;
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
public class UserServiceImpl implements UserService {

    private UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
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
        user.setTasks(u.getTasks());
        user.setId(id);
        return repository.save(user);
    }
}
