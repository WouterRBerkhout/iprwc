package nl.birchwood.iprwc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public <T> EntityNotFoundException(Class<T> entity) {
        super("Entity of type '" + entity.getSimpleName() + "' not found");
    }

}
