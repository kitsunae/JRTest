package net.example.root.service;

import net.example.config.JpaConfig;
import net.example.config.RootConfig;
import net.example.root.domain.Task;
import net.example.root.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by lashi on 25.02.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class, JpaConfig.class})
public class ServiceImplTest {

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    private List<User> users = new ArrayList<>();

    @org.junit.Before
    @Transactional
    public void setUp() throws Exception {
        List<Task> tasksOfFirstUser = new ArrayList<>();
        tasksOfFirstUser.add(new Task("First Test Title", "Test task text", false));
        tasksOfFirstUser.add(new Task("Second Test Title", "Test second task text", false));
        User firstUser = new User("Josua", "Bloh", "JBo", "foo");
        firstUser.setTasks(tasksOfFirstUser);
        List<Task> tasksOfSecondUser = new ArrayList<>();
        tasksOfSecondUser.add(new Task("Third Test Title", "Third test text", false));
        tasksOfSecondUser.add(new Task("Forth Test Title", "Forth test text", true));
        tasksOfSecondUser.add(new Task("Fifth Test Title", "Fifth test text", false));
        User secondUser = new User("Mickael", "Jackson", "Mjack", "baz");
        secondUser.setTasks(tasksOfSecondUser);
        users.add(firstUser);
        users.add(secondUser);
        userService.save(firstUser);
        userService.save(secondUser);
    }

    @Test
    @Transactional
    public void addingTest(){
        Task task = new Task("Sixth Test Title", "Sixth Test Text", false);
        User user = userService.getUserByLogin("JBo");
        task = taskService.save(task, user.getId());
        assertNotNull(task.getId());
        assertNotNull(task.getCreatedAt());
        assertEquals(user, task.getUser());
        User newUser = new User("Anton", "Lashin", "kits", "1234");
        newUser = userService.save(newUser);
        assertNotNull(newUser.getId());
        assertEquals(0, newUser.getTasks().size());
        Task anotherTask = new Task("Seventh Test Title", "Seventh Test Text", true);
        anotherTask = taskService.save(anotherTask, newUser.getId());
        assertNotNull(anotherTask.getId());
        assertEquals(newUser, anotherTask.getUser());
    }

    @Test
    @Transactional
    public void editingTest(){
        long id = userService.getUserByLogin("JBo").getId();
        User editedUser = new User("Josua", "Blogh", "JBoo", null);
        userService.edit(editedUser, id);
        User user = userService.getUser(id);
        Date createdAt = user.getTasks().get(0).getCreatedAt();
        long taskId = user.getTasks().get(0).getId();
        assertEquals("foo", user.getPassword());
        assertEquals("JBoo", user.getLogin());
        assertEquals("Blogh", user.getSurname());
        assertEquals("Josua", user.getName());
        assertEquals(2, user.getTasks().size());
        assertNull(userService.getUserByLogin("JBo"));
        Task task = new Task("First Test Title", "Test edited task text", true, createdAt, editedUser);
        task = taskService.edit(task, taskId);
        assertEquals(user, task.getUser());
        assertEquals(user.getId(), task.getUser().getId());
        assertEquals(2, userService.getUser(id).getTasks().size());
        assertTrue(userService.getUser(id).getTasks().contains(task));
        assertEquals(task.getText(), userService.getUser(id).getTasks().get(0).getText());
    }

    @Test
    @Transactional
    public void removalTest(){
        User toDelete = userService.getUserByLogin("Mjack");
        userService.remove(toDelete.getId());
        assertEquals(0, taskService.getTasks(0,100, toDelete.getId()).size());
        assertNull(userService.getUser(toDelete.getId()));
    }
}