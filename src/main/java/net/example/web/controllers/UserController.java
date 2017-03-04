package net.example.web.controllers;

import net.example.root.domain.User;
import net.example.root.hateoas.UserResource;
import net.example.root.service.UserService;
import net.example.web.exceptions.AccessDeniedException;
import net.example.web.exceptions.UserNotFoundException;
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
@RequestMapping(value = "/user")
public class UserController {

    private UserService service;
    private ResourceAssemblerSupport<User, UserResource> resourceAssembler;

    @Autowired
    public UserController(UserService service, ResourceAssemblerSupport<User, UserResource> resourceAssembler) {
        this.service = service;
        this.resourceAssembler = resourceAssembler;
    }

    @RequestMapping(value = "/all/count", method = RequestMethod.GET)
    public Long getNumberOfPages(){
        return service.getNumberOfAllUsers()/DefaultPageParameters.RESULTS_ON_PAGE;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserResource getUser(@PathVariable long id){
        User user = service.getUser(id);
        if (user == null)
            throw new UserNotFoundException();
        return resourceAssembler.toResource(user);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<UserResource> getAllUsers(@RequestParam(value = "page", defaultValue = DefaultPageParameters.START_PAGE_STRING)int page,
                                          @RequestParam(value = "size", defaultValue = DefaultPageParameters.RESULTS_ON_PAGE_STRING) int size){
        return service.getUsers(page, size)
                .stream()
                .map(user -> resourceAssembler.toResource(user))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    public UserResource save(@RequestBody UserResource user){
        User result = service.save(user.toUser());
        return resourceAssembler.toResource(result);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public UserResource remove(@PathVariable long id){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            String login = ((UserDetails) principal).getUsername();
            if (id==service.getUserByLogin(login).getId()){
                User user = service.remove(id);
                if (user==null)
                    throw new UserNotFoundException();
                return resourceAssembler.toResource(user);
            }else{
                throw new AccessDeniedException();
            }
        }else{
            throw new AccessDeniedException();
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public UserResource edit(@RequestBody UserResource user,
                             @PathVariable long id){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            String login = ((UserDetails) principal).getUsername();
            if (id == service.getUserByLogin(login).getId()){
                return resourceAssembler.toResource(service.edit(user.toUser(), id));
            }else{
                throw new AccessDeniedException();
            }
        }else{
            throw new AccessDeniedException();
        }
    }
}
