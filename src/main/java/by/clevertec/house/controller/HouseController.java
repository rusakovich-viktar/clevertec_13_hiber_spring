package by.clevertec.house.controller;

import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.service.HouseService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для работы с домами.
 * Обрабатывает HTTP-запросы, связанные с домами.
 */
@RestController
@RequestMapping("/houses")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    /**
     * Получает информацию о доме по его UUID.
     *
     * @param uuid UUID дома.
     * @return ResponseEntity с информацией о доме.
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<HouseResponseDto> getHouseByUuid(@PathVariable UUID uuid) {
        HouseResponseDto house = houseService.getHouseByUuid(uuid);
        return ResponseEntity.ok(house);
    }

    /**
     * Получает список всех домов с пагинацией.
     *
     * @param pageNumber номер страницы.
     * @param pageSize   размер страницы.
     * @return ResponseEntity со списком домов.
     */
    @GetMapping
    public ResponseEntity<List<HouseResponseDto>> getAllHouses(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "15") int pageSize) {
        List<HouseResponseDto> houses = houseService.getAllHouses(pageNumber, pageSize);
        return houses.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(houses);
    }

    /**
     * Сохраняет информацию о новом доме.
     *
     * @param house DTO с информацией о доме.
     * @return ResponseEntity с кодом статуса CREATED.
     */
    @PostMapping
    public ResponseEntity<Void> saveHouse(@Valid @RequestBody HouseRequestDto house) {
        houseService.saveHouse(house);
        return ResponseEntity.created(null).build();
    }

    /**
     * Обновляет информацию о существующем доме по его UUID.
     *
     * @param uuid  UUID дома.
     * @param house DTO с новой информацией о доме.
     * @return ResponseEntity с кодом статуса OK.
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<Void> updateHouse(@PathVariable UUID uuid,
                                            @Valid @RequestBody HouseRequestDto house) {
        houseService.updateHouse(uuid, house);
        return ResponseEntity.ok().build();
    }

    /**
     * Обновляет определенные поля дома по его UUID.
     *
     * @param uuid    UUID дома.
     * @param updates Map с обновлениями полей.
     * @return ResponseEntity с кодом статуса OK.
     */
    @PatchMapping("/{uuid}")
    public ResponseEntity<Void> updateHouseFields(@PathVariable UUID uuid, @Valid @RequestBody Map<String, Object> updates) {
        houseService.updateHouseFields(uuid, updates);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаляет дом по его UUID.
     *
     * @param uuid UUID дома.
     * @return ResponseEntity с кодом статуса NO_CONTENT.
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteHouse(@PathVariable UUID uuid) {
        houseService.deleteHouse(uuid);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получает список жителей дома по его UUID.
     *
     * @param uuid UUID дома.
     * @return ResponseEntity со списком жителей дома.
     */
    @GetMapping("/{uuid}/residents")
    public ResponseEntity<List<PersonResponseDto>> getResidents(@PathVariable UUID uuid) {
        List<PersonResponseDto> residents = houseService.getResidents(uuid);
        return residents.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(residents);
    }
}
