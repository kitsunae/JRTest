package net.example.root.hateoas.asm;

import net.example.root.domain.User;
import net.example.root.hateoas.UserResource;
import net.example.web.controllers.TaskController;
import net.example.web.controllers.UserController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by lashi on 24.02.2017.
 */
@Component
public class UserResourceAsm extends ResourceAssemblerSupport<User, UserResource> {

    public UserResourceAsm() {
        super(UserController.class, UserResource.class);
    }

    @Override
    public UserResource toResource(User user) {
        UserResource resource = new UserResource();
        resource.setNumber(user.getId());
        resource.setName(user.getName());
        resource.setLogin(user.getLogin());
        resource.setSurname(user.getSurname());
        resource.setPassword(user.getPassword());
        Link self = linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel();
        resource.add(self);
        Link tasks = linkTo(methodOn(TaskController.class).getAllTasks(null, null, user.getId())).withRel("tasks");
        resource.add(tasks);
        Link users = linkTo(methodOn(UserController.class).getAllUsers(null, null)).withRel("users");
        resource.add(users);
        Link doneTasks = linkTo(methodOn(TaskController.class).getDoneTasks(null, null, user.getId(), true)).withRel("done-tasks");
        resource.add(doneTasks);
        Link undoneTasks = linkTo(methodOn(TaskController.class).getDoneTasks(null, null, user.getId(), false)).withRel("undone-tasks");
        resource.add(undoneTasks);
        return resource;
    }
}
