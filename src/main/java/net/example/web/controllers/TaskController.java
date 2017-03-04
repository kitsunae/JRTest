package net.example.web.controllers;

import net.example.root.domain.Task;
import net.example.root.hateoas.TaskResource;
import net.example.root.hateoas.asm.TaskResourceAsm;
import net.example.root.service.TaskService;
import net.example.root.service.UserService;
import net.example.web.exceptions.AccessDeniedException;
import net.example.web.exceptions.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lashi on 24.02.2017.
 */
@RestController
@RequestMapping(value = "/task")
public class TaskController {

    private TaskService service;
    private ResourceAssemblerSupport<Task, TaskResource> resourceAssembler;
    private UserService userService;

    @Autowired
    public TaskController(TaskService service, ResourceAssemblerSupport<Task, TaskResource> resourceAssembler, UserService userService) {
        this.service = service;
        this.resourceAssembler = resourceAssembler;
        this.userService = userService;
    }

    @RequestMapping(value = "/all/count")
    public Long getNumberOfPages(@RequestParam(value = "tasksOnPage", defaultValue = DefaultPageParameters.RESULTS_ON_PAGE_STRING) int tasksOnPage,
                                @RequestParam(value = "userId", defaultValue = "-1") long userId){
        return service.getNumberOfAllTasksByUser(userId)/tasksOnPage + 1;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<TaskResource> getAllTasks(@RequestParam(value = "page", defaultValue = DefaultPageParameters.START_PAGE_STRING)int page,
                                          @RequestParam(value = "size", defaultValue = DefaultPageParameters.RESULTS_ON_PAGE_STRING) int size,
                                          @RequestParam(value = "userId") long userId){
        return service.getTasks(page, size, userId)
                .stream()
                .map(task -> resourceAssembler.toResource(task))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public TaskResource getTask(@PathVariable long id){
        Task task = service.getTask(id);
        if (task==null)
            throw new TaskNotFoundException();
        return new TaskResourceAsm().toResource(task);
    }

    @RequestMapping(value = "/done/{isDone}")
    public List<TaskResource> getDoneTasks(@RequestParam(value = "page", defaultValue = DefaultPageParameters.START_PAGE_STRING) int page,
                                           @RequestParam(value = "size", defaultValue = DefaultPageParameters.RESULTS_ON_PAGE_STRING) int size,
                                           @RequestParam(value = "userId") long userId,
                                           @PathVariable boolean isDone){
        return service.getDoneTasks(page, size, userId, isDone)
                .stream()
                .map(task -> resourceAssembler.toResource(task))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    public TaskResource save(@RequestBody TaskResource task,
                             @RequestParam(value = "userId") long userId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            String login = ((UserDetails) principal).getUsername();
            if (userId==userService.getUserByLogin(login).getId()){
                return resourceAssembler.toResource(service.save(task.toTask(), userId));
            }else {
                throw new AccessDeniedException();
            }
        }else {
            throw new AccessDeniedException();
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public TaskResource edit(@RequestBody TaskResource task, @PathVariable long id){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            String login = ((UserDetails) principal).getUsername();
            if (task.getUser()==null)
                throw new AccessDeniedException();
            if (login.equals(task.getUser().getLogin()) && userService.getUserByLogin(login).getId().equals(task.getUser().getNumber()))
                return resourceAssembler.toResource(service.edit(task.toTask(), id));
            else{
                throw new AccessDeniedException();
            }
        }else{
            throw new AccessDeniedException();
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public TaskResource remove(@PathVariable long id,
                               @RequestParam(value = "userId") long userId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            String login = ((UserDetails) principal).getUsername();
            if (userId==userService.getUserByLogin(login).getId()){
                return resourceAssembler.toResource(service.remove(id));
            }else {
                throw new AccessDeniedException();
            }
        }else {
            throw new AccessDeniedException();
        }
    }
}
