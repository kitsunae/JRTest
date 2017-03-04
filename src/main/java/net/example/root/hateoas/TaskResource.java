package net.example.root.hateoas;

import net.example.root.domain.Task;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * Created by lashi on 24.02.2017.
 */
public class TaskResource extends ResourceSupport {

    private Long number;
    private String title;
    private String text;
    private boolean done;
    private Date createdAt;
    private UserResource user;

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UserResource getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(UserResource user) {
        this.user = user;
    }

    public Task toTask(){
        return user==null ? new Task(title, text, done, createdAt) : new Task(title, text, done, createdAt, user.toUser());
    }
}
