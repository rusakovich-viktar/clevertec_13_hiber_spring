package by.clevertec.house.dto;

import by.clevertec.house.entity.Sex;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для запроса на создание или обновление персоны.
 * Содержит информацию о персоне и его связях с домами.
 */
@Data
@NoArgsConstructor
public class PersonRequestDto {
    private UUID uuid;
    @NotNull(message = "Поле 'name' не может быть null")
    private String name;
    @NotNull(message = "Поле 'surname' не может быть null")
    private String surname;
    @NotNull(message = "Поле 'sex' не может быть null")
    private Sex sex;
    @Valid
    @NotNull(message = "Поле 'passportData' не может быть null")
    private PassportDataDto passportData;
    private UUID houseUuid;
    private List<UUID> ownedHouseUuids;

    /**
     * Вложенный класс DTO для данных паспорта персоны.
     */
    @Data
    public static class PassportDataDto {
        @Size(min = 2, max = 2, message = "Длина серии паспорта должна быть ровно 2 символа")
        @Pattern(regexp = "[A-Z]{2}")
        private String passportSeries;

        @Size(min = 7, max = 7, message = "Длина номера паспорта должна быть ровно 7 символов")
        private String passportNumber;
    }
}
