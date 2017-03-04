package net.example.web.controllers;

import net.example.root.domain.Task;
import net.example.root.domain.User;
import net.example.root.hateoas.asm.TaskResourceAsm;
import net.example.root.service.TaskService;
import net.example.web.exceptions.TaskNotFoundException;
import net.example.web.exceptions.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by lashi on 25.02.2017.
 */
public class TaskControllerTest {

    @Mock
    private TaskService service;

    private TaskResourceAsm resourceAsm;
    private TaskController taskController;
    private MockMvc mockMvc;

    private Task task1;
    private Task task2;
    private Task task3;
    private User user;
    private List<Task> taskList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        user = new User("Anton", "lashin", "kit", "1234");
        user.setId(1L);
        task1 = new Task("Test Title", "Test Text", false, new Date(), user);
        task1.setId(1L);
        task2 = new Task("Test Title 2", "Test Text 2", true, new Date(), user);
        task2.setId(2L);
        taskList.add(task1);
        taskList.add(task2);
        task3 = new Task("Test Title 3", "Test Text 3", false);
        task3.setId(3L);
        user.setTasks(taskList);
        when(service.getTask(1L)).thenReturn(task1);
        when(service.getTask(2L)).thenReturn(task2);
        when(service.getTasks(0,20,1L)).thenReturn(taskList);
        when(service.getDoneTasks(0, 20, 1L, true)).thenReturn(Collections.singletonList(task2));
        when(service.getDoneTasks(0, 20, 1L, false)).thenReturn(Collections.singletonList(task1));
        when(service.getNumberOfAllTasksByUser(1L)).thenReturn(2L);
        when(service.save(task3, 1L)).thenReturn(task3);
        when(service.save(task3, -1)).thenThrow(new UserNotFoundException());
        when(service.remove(-1)).thenThrow(new TaskNotFoundException());
        resourceAsm = new TaskResourceAsm();
        taskController = new TaskController(service, resourceAsm);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    public void getNumberOfPages() throws Exception {
        mockMvc.perform(get("/task/all/count")
                .param("tasksOnPage", "20")
                .param("userId", "1"))
                .andDo(print())
                .andExpect(content().string("1"));
    }

    @Test
    public void getAllTasks() throws Exception {
        mockMvc.perform(get("/task/all")
                .param("page", "0")
                .param("size", "20")
                .param("userId", "1"))
                .andDo(print())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].title", is("Test Title")))
                .andExpect(jsonPath("$[0].text", is("Test Text")))
                .andExpect(jsonPath("$[0].done", is(false)))
                .andExpect(jsonPath("$[0].user.name", is("Anton")))
                .andExpect(jsonPath("$[0].user.surname", is("lashin")));
    }

    @Test
    public void getTask() throws Exception {
        mockMvc.perform(get("/task/1"))
                .andDo(print())
                .andExpect(jsonPath("$.title", is("Test Title")))
                .andExpect(jsonPath("$.text", is("Test Text")))
                .andExpect(jsonPath("$.user.name", is("Anton")))
                .andExpect(jsonPath("$.user.surname", is("lashin")))
                .andExpect(jsonPath("$.links[0].href", is("http://localhost/task/1")));
    }

    @Test
    public void getDoneTasks() throws Exception {
        //TODO null tests, exceptions tests
    }

    @Test
    public void save() throws Exception {

    }

    @Test
    public void edit() throws Exception {

    }

    @Test
    public void remove() throws Exception {

    }

}