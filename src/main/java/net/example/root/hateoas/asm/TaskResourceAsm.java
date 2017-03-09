package net.example.root.hateoas.asm;

import net.example.root.domain.Task;
import net.example.root.hateoas.TaskResource;
import net.example.web.controllers.TaskController;
import net.example.web.controllers.UserController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class TaskResourceAsm extends ResourceAssemblerSupport<Task, TaskResource> {

    public TaskResourceAsm() {
        super(TaskController.class, TaskResource.class);
    }

    @Override
    public TaskResource toResource(Task task) {
        TaskResource resource = new TaskResource();
        resource.setNumber(task.getId());
        resource.setText(task.getText());
        resource.setCreatedAt(task.getCreatedAt());
        resource.setDone(task.isDone());
        resource.setUser(new UserResourceAsm().toResource(task.getUser()));
        resource.setTitle(task.getTitle());
        Link self = linkTo(methodOn(TaskController.class).getTask(task.getId())).withSelfRel();
        resource.add(self);
        Link user = linkTo(methodOn(UserController.class).getUser(task.getUser().getId())).withRel("user");
        resource.add(user);
        Link tasks = linkTo(methodOn(TaskController.class).getAllTasks(null, null, task.getUser().getId())).withRel("tasks");
        resource.add(tasks);
        return resource;
    }
}
