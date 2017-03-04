package net.example.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by lashi on 25.02.2017.
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Login already exists")
public class LoginNotAvailableException extends RuntimeException {
}
