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

    @RequestMapping(value = "/count")
    public Long getNumberOfTasks(@RequestParam(value = "userId") long userId){
        return service.getNumberOfAllTasksByUser(userId);
    }

    @RequestMapping(value = "/all/{userId}", method = RequestMethod.GET)
    public List<TaskResource> getAllTasks(@RequestParam(value = "page", required = false)Integer page,
                                              @RequestParam(value = "size", required = false) Integer size,
                                              @PathVariable Long userId){
        if (page!=null && size!=null){
            return service.getTasks(page, size, userId)
                    .stream()
                    .map(task -> resourceAssembler.toResource(task))
                    .collect(Collectors.toList());
        }
        return service.getAllTasks(userId)
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
    public List<TaskResource> getDoneTasks(@RequestParam(value = "page", required = false) Integer page,
                                           @RequestParam(value = "size", required = false) Integer size,
                                           @RequestParam(value = "userId") long userId,
                                           @PathVariable boolean isDone){
        if (page!=null && size!=null){
            return service.getDoneTasks(page, size, userId, isDone)
                    .stream()
                    .map(task -> resourceAssembler.toResource(task))
                    .collect(Collectors.toList());
        }
        return service.getAllDoneTasks(userId, isDone)
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
