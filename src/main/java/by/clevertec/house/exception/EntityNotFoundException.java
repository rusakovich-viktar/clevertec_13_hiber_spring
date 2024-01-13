package by.clevertec.house.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public static EntityNotFoundException of(Class<?> clazz, Object field) {
        return new EntityNotFoundException(clazz.getSimpleName() + " with UUID " + field + " does not exist");
    }
}
