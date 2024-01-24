package by.clevertec.house.dto;

import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Поле 'uuid' не может быть null")
    private UUID uuid;
    @NotNull(message = "Поле 'area' не может быть null")
    private Double area;

    @NotNull(message = "Поле 'country' не может быть null")
    private String country;

    @NotNull(message = "Поле 'city' не может быть null")
    private String city;

    @NotNull(message = "Поле 'street' не может быть null")
    private String street;

    @NotNull(message = "Поле 'number' не может быть null")
    private String number;
    private List<UUID> residentUuids;
    private List<UUID> ownerUuids;
}
