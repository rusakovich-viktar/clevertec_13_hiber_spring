package by.clevertec.house.exception;

import static by.clevertec.house.util.Constant.ErrorMessages.ENTITY_NOT_FOUND;
import static by.clevertec.house.util.Constant.ErrorMessages.INVALID_ARGUMENTS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import by.clevertec.house.util.Constant.ErrorMessages;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Глобальный обработчик исключений.
 * Обрабатывает исключения, возникающие во время выполнения приложения, и возвращает соответствующие сообщения об ошибках.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключения валидации методов.
     *
     * @param exception Исключение MethodArgumentNotValidException.
     * @return ResponseEntity с сообщением об ошибке валидации.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = new ErrorResponse(errorMessage, ErrorMessages.VALIDATION_ERROR);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает исключения недопустимых аргументов.
     *
     * @param exception Исключение IllegalArgumentException.
     * @return ResponseEntity с сообщением об ошибке недопустимых аргументов.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException exception) {
        return getErrorResponseEntity(exception, INVALID_ARGUMENTS, BAD_REQUEST);
    }

    /**
     * Обрабатывает исключения, когда сущность не найдена.
     *
     * @param exception Исключение EntityNotFoundException.
     * @return ResponseEntity с сообщением об ошибке, что сущность не найдена.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(EntityNotFoundException exception) {
        return getErrorResponseEntity(exception, ENTITY_NOT_FOUND, NOT_FOUND);
    }

    /**
     * Обрабатывает исключения null-указателя.
     *
     * @param exception Исключение NullPointerException.
     * @return ResponseEntity с сообщением об ошибке null-указателя.
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleException(NullPointerException exception) {
        return getErrorResponseEntity(exception, INVALID_ARGUMENTS, BAD_REQUEST);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleException(DataIntegrityViolationException exception) {
        return getErrorResponseEntity(exception, INVALID_ARGUMENTS, BAD_REQUEST);
    }

    /**
     * Обрабатывает все остальные исключения.
     *
     * @param exception Исключение Exception.
     * @return ResponseEntity с сообщением об общей ошибке сервера.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return getErrorResponseEntity(exception, ErrorMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Создает ResponseEntity с сообщением об ошибке.
     *
     * @param exception     Исключение, которое нужно обработать.
     * @param errorMessages Сообщение об ошибке.
     * @param httpStatus    Статус HTTP-ответа.
     * @return ResponseEntity с сообщением об ошибке.
     */
    private static ResponseEntity<ErrorResponse> getErrorResponseEntity(Exception exception, String errorMessages, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(), errorMessages), httpStatus);
    }
}
