package net.example.root.dao;

import net.example.root.domain.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long id, Pageable pageable);
    Long countByUserId(Long id);
    List<Task> findByUserIdAndDone(Long id, Boolean done, Pageable pageable);
    Task findByTitleAndText(String title, String text);
    List<Task> findByUserId(Long userId);
    List<Task> findByUserIdAndDone(Long userId, Boolean isDone);
}
