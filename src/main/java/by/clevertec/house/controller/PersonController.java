package by.clevertec.house.controller;

import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.HouseWithHistoryDto;
import by.clevertec.house.dto.PersonRequestDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.service.PersonService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
 * Контроллер для работы с персонами.
 * Обрабатывает HTTP-запросы, связанные с персонами.
 */
@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    /**
     * Получает информацию о персоне по его UUID.
     *
     * @param uuid UUID персоны.
     * @return ResponseEntity с информацией о персоне.
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<PersonResponseDto> getPersonByUuid(@PathVariable UUID uuid) {
        PersonResponseDto person = personService.getPersonByUuid(uuid);
        return ResponseEntity.ok(person);
    }

    /**
     * Получает список всех персон с пагинацией.
     *
     * @param pageNumber номер страницы.
     * @param pageSize   размер страницы.
     * @return ResponseEntity со списком персон.
     */
    @GetMapping
    public ResponseEntity<List<PersonResponseDto>> getAllPersons(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "15") int pageSize) {
        List<PersonResponseDto> persons = personService.getAllPersons(pageNumber, pageSize);
        return ResponseEntity.ok(persons);
    }

    /**
     * Сохраняет информацию о новой персоне.
     *
     * @param person DTO с информацией о персоне.
     * @return ResponseEntity с кодом статуса CREATED.
     */
    @PostMapping
    public ResponseEntity<Void> savePerson(@Valid @RequestBody PersonRequestDto person) {
        personService.savePerson(person);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Обновляет информацию о существующей персоне по его UUID.
     *
     * @param uuid   UUID персоны.
     * @param person DTO с новой информацией о персоне.
     * @return ResponseEntity с кодом статуса OK.
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<Void> updatePerson(@PathVariable UUID uuid,
                                             @Valid @RequestBody PersonRequestDto person) {
        personService.updatePerson(uuid, person);
        return ResponseEntity.ok().build();
    }

    /**
     * Обновляет определенные поля персоны по его UUID.
     *
     * @param uuid    UUID персоны.
     * @param updates Map с обновлениями полей.
     * @return ResponseEntity с кодом статуса OK.
     */
    @PatchMapping("/{uuid}")
    public ResponseEntity<Void> updatePersonFields(@PathVariable UUID uuid, @Valid @RequestBody Map<String, Object> updates) {
        personService.updatePersonFields(uuid, updates);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаляет персону по его UUID.
     *
     * @param uuid UUID персоны.
     * @return ResponseEntity с кодом статуса NO_CONTENT.
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deletePerson(@PathVariable UUID uuid) {
        personService.deletePerson(uuid);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получает список домов, принадлежащих персоне по его UUID.
     *
     * @param uuid UUID персоны.
     * @return ResponseEntity со списком домов, принадлежащих персоне.
     */
    @GetMapping("/{uuid}/owned-houses")
    public ResponseEntity<List<HouseResponseDto>> getOwnedHouses(@PathVariable UUID uuid) {
        List<HouseResponseDto> ownedHouses = personService.getOwnedHousesByPersonUuid(uuid);
        return ResponseEntity.ok(ownedHouses);

    }

    /**
     * Получает список домов, ранее принадлежащих персоне по его UUID.
     */
    @GetMapping("/{uuid}/tenanted-houses/history")
    public ResponseEntity<List<HouseWithHistoryDto>> getPastResidences(@PathVariable UUID uuid) {
        List<HouseWithHistoryDto> pastTenants = personService.getPastTenantsByUuid(uuid);
        return ResponseEntity.ok(pastTenants);
    }

    /**
     * Получает список домов, которе ранее принадлежали персоне по его UUID.
     */
    @GetMapping("/{uuid}/owned-houses/history")
    public ResponseEntity<List<HouseWithHistoryDto>> getPastOwnedHouses(@PathVariable UUID uuid) {
        List<HouseWithHistoryDto> ownedHouses = personService.getPastOwnedHousesByUuid(uuid);
        return ResponseEntity.ok(ownedHouses);
    }
}
