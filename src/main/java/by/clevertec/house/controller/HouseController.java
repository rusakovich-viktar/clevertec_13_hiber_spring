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

@RestController
@RequestMapping("/houses")
@RequiredArgsConstructor
public class HouseController {
    private final HouseService houseService;

    @GetMapping("/{uuid}")
    public ResponseEntity<HouseResponseDto> getHouseByUuid(@PathVariable UUID uuid) {
        HouseResponseDto house = houseService.getHouseByUuid(uuid);
        return ResponseEntity.ok(house);
    }

    @GetMapping
    public ResponseEntity<List<HouseResponseDto>> getAllHouses(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "15") int pageSize) {
        List<HouseResponseDto> houses = houseService.getAllHouses(pageNumber, pageSize);
        return houses.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(houses);
    }

    @PostMapping
    public ResponseEntity<Void> saveHouse(@Valid @RequestBody HouseRequestDto house) {
        houseService.saveHouse(house);
        return ResponseEntity.created(null).build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Void> updateHouse(@PathVariable UUID uuid,
                                            @Valid @RequestBody HouseRequestDto house) {
        houseService.updateHouse(uuid, house);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<Void> updateHouseFields(@PathVariable UUID uuid, @Valid @RequestBody Map<String, Object> updates) {
        houseService.updateHouseFields(uuid, updates);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteHouse(@PathVariable UUID uuid) {
        houseService.deleteHouse(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{uuid}/residents")
    public ResponseEntity<List<PersonResponseDto>> getResidents(@PathVariable UUID uuid) {
        List<PersonResponseDto> residents = houseService.getResidents(uuid);
        return residents.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(residents);
    }
}
