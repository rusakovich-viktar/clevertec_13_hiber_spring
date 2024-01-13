package by.clevertec.house.controller;

import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.PersonRequestDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.entity.HouseEntity;
import by.clevertec.house.exception.EntityNotFoundException;
import by.clevertec.house.service.PersonService;
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

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;


    @GetMapping("/{uuid}")
    public ResponseEntity<PersonResponseDto> getPersonByUuid(@PathVariable UUID uuid) {
        PersonResponseDto person = personService.getPersonByUuid(uuid);
        return ResponseEntity.ok(person);
    }

    @GetMapping
    public ResponseEntity<List<PersonResponseDto>> getAllPersons(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "15") int pageSize) {
        List<PersonResponseDto> persons = personService.getAllPersons(pageNumber, pageSize);
        if (persons.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(persons);
    }

    @PostMapping
    public ResponseEntity<Void> savePerson(@RequestBody PersonRequestDto person) {
        personService.savePerson(person);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Void> updatePerson(@PathVariable UUID uuid,
                                             @RequestBody PersonRequestDto person) {
        personService.updatePerson(uuid, person);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PatchMapping("/{uuid}")
    public ResponseEntity<Void> updatePersonFields(@PathVariable UUID uuid, @RequestBody Map<String, Object> updates) {
        personService.updatePersonFields(uuid, updates);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deletePerson(@PathVariable UUID uuid) {
        personService.deletePerson(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{uuid}/ownedHouses")
    public ResponseEntity<List<HouseResponseDto>> getOwnedHouses(@PathVariable UUID uuid) {
        List<HouseResponseDto> ownedHouses = personService.getOwnedHouses(uuid);
        if (ownedHouses.isEmpty()) {
            throw EntityNotFoundException.of(HouseEntity.class, uuid);
        }
        return ResponseEntity.ok(ownedHouses);
    }
}
