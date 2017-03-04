package net.example.web.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by lashi on 25.02.2017.
 */
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {TaskNotFoundException.class, UserNotFoundException.class})
    public String notFoundHandler(){
        return "/error";
    }

    @ExceptionHandler(value = {LoginNotAvailableException.class, AccessDeniedException.class})
    public String accessHandler(){
        return "/";
    }
}
