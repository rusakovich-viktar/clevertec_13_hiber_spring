package by.clevertec.house.dto;

import by.clevertec.house.entity.PassportData;
import by.clevertec.house.entity.Sex;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для ответа с информацией о персоне.
 * Содержит информацию о персоне и его связях с домами.
 */
@Data
@NoArgsConstructor
public class PersonResponseDto {

    private UUID uuid;
    private String name;
    private String surname;
    private Sex sex;
    private PassportData passportData;
    private String createDate;
    private String updateDate;

}
