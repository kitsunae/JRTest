package net.example.root.dao;

import net.example.root.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lashi on 24.02.2017.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
}
