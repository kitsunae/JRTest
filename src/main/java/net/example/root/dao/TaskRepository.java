package net.example.root.dao;

import net.example.root.domain.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by lashi on 24.02.2017.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long id, Pageable pageable);
    Long countByUserId(Long id);
    List<Task> findByUserIdAndDone(Long id, Boolean done, Pageable pageable);
    Task findByTitleAndText(String title, String text);
}
