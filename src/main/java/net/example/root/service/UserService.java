package net.example.root.service;

import net.example.root.domain.User;

import java.util.List;

/**
 * Created by lashi on 24.02.2017.
 */
public interface UserService {
    User getUser(long id);
    List<User> getUsers(int page, int size);
    User save(User user);
    User remove(long id);
    Long getNumberOfAllUsers();
    User getUserByLogin(String login);
    User edit(User user, long id);
}
