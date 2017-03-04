package net.example.root.domain;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lashi on 24.02.2017.
 */
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String text;
    private boolean done;
    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public Task(){}

    public Task(String title, String text, boolean done, Date createdAt) {
        this.title = title;
        this.text = text;
        this.done = done;
        this.createdAt = createdAt;
    }

    public Task(String title, String text, boolean done, Date createdAt, User user) {
        this.title = title;
        this.text = text;
        this.done = done;
        this.createdAt = createdAt;
        this.user = user;
    }

    public Task(String title, String text, boolean done) {
        this.title = title;
        this.text = text;
        this.done = done;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (getTitle() != null ? !getTitle().equals(task.getTitle()) : task.getTitle() != null) return false;
        if (getCreatedAt() != null ? !getCreatedAt().equals(task.getCreatedAt()) : task.getCreatedAt() != null)
            return false;
        return getUser() != null ? getUser().equals(task.getUser()) : task.getUser() == null;

    }

    @Override
    public int hashCode() {
        int result = getTitle() != null ? getTitle().hashCode() : 0;
        result = 31 * result + (getCreatedAt() != null ? getCreatedAt().hashCode() : 0);
        result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
        return result;
    }
}
