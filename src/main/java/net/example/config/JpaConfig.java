package net.example.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by lashi on 24.02.2017.
 */
@Configuration
@EnableJpaRepositories(basePackages = {"net.example.root.dao"})
public class JpaConfig {

}
