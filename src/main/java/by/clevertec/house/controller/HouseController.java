package by.clevertec.house.controller;

import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.service.HouseService;
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
        if (houses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(houses);
    }

    @PostMapping
    public ResponseEntity<Void> saveHouse(@RequestBody HouseRequestDto house) {
        houseService.saveHouse(house);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Void> updateHouse(@PathVariable UUID uuid, @RequestBody HouseRequestDto house) {
            houseService.updateHouse(uuid, house);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<Void> updateHouseFields(@PathVariable UUID uuid, @RequestBody Map<String, Object> updates) {
        try {
            houseService.updateHouseFields(uuid, updates);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteHouse(@PathVariable UUID uuid) {
        try {
            houseService.deleteHouse(uuid);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{uuid}/residents")
    public ResponseEntity<List<PersonResponseDto>> getResidents(@PathVariable UUID uuid) {
        List<PersonResponseDto> residents = houseService.getResidents(uuid);
        if (residents.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(residents);
    }
}
