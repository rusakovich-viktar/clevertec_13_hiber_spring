package by.clevertec.house.dto;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для ответа с информацией о доме.
 * Содержит информацию о доме и его связях с персонами.
 */
@Data
@NoArgsConstructor
public class HouseResponseDto {

    private UUID uuid;
    private double area;
    private String country;
    private String city;
    private String street;
    private String number;
    private String createDateIso;
}
