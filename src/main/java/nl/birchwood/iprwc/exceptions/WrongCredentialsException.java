package nl.birchwood.iprwc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class WrongCredentialsException extends RuntimeException {
    public WrongCredentialsException() {
        super("Username and/ or password is incorrect");
    }
}
