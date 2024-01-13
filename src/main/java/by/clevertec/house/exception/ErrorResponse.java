package by.clevertec.house.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String timestamp;
    private String message;
    private String details;

    public ErrorResponse(String message, String details) {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.message = message;
        this.details = details;
    }

}
