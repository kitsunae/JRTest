package net.example.config;

import net.example.security.AuthFailure;
import net.example.security.AuthSuccess;
import net.example.security.EntryPointUnauthorized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@ComponentScan(basePackages = {"net.example.security"})
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private AuthSuccess authSuccess;
    @Autowired
    private AuthFailure authFailure;
    @Autowired
    private EntryPointUnauthorized entryPointUnauthorized;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configAuthBuilder(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(entryPointUnauthorized)
                .and()
                .formLogin()
                .successHandler(authSuccess)
                .failureHandler(authFailure)
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .authorizeRequests()
                .antMatchers("/**")
                .permitAll();
    }
}
