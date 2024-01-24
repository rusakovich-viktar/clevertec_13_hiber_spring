package by.clevertec.house.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для запроса на создание или обновление дома.
 * Содержит информацию о доме и его связях с персонами.
 */
@Data
@NoArgsConstructor
public class HouseRequestDto {

    private UUID uuid = UUID.randomUUID();

    @NotNull(message = "Поле 'area' не может быть null")
    @Positive(message = "Поле 'area' должно быть больше нуля")
    private Double area;

    @NotEmpty(message = "Поле 'country' не может быть пустым")
    private String country;

    @NotEmpty(message = "Поле 'city' не может быть пустым")
    private String city;

    @NotEmpty(message = "Поле 'street' не может быть пустым")
    private String street;

    @NotEmpty(message = "Поле 'number' не может быть пустым")
    private String number;

    private List<UUID> residentUuids;
    private List<UUID> ownerUuids;
}
