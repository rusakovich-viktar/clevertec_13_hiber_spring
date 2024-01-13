package by.clevertec.house.exception;

import static by.clevertec.house.util.Constants.ErrorMessages.ENTITY_NOT_FOUND;
import static by.clevertec.house.util.Constants.ErrorMessages.INTERNAL_SERVER_ERROR;
import static by.clevertec.house.util.Constants.ErrorMessages.INVALID_ARGUMENTS;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException exception) {
        return getErrorResponseEntity(exception, INVALID_ARGUMENTS, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(EntityNotFoundException exception) {
        return getErrorResponseEntity(exception, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return getErrorResponseEntity(exception, INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ResponseEntity<ErrorResponse> getErrorResponseEntity(Exception exception, String errorMessages, HttpStatus internalServerError1) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(), errorMessages), internalServerError1);
    }
}
