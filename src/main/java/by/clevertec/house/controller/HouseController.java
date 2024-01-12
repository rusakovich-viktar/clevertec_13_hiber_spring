package by.clevertec.house.controller;

import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.service.HouseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/{id}")
    public ResponseEntity<HouseResponseDto> getHouseById(@PathVariable Long id) {
        HouseResponseDto house = houseService.getHouseById(id);
        return new ResponseEntity<>(house, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<HouseResponseDto>> getAllHouses(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "15") int pageSize) {
        List<HouseResponseDto> houses = houseService.getAllHouses(pageNumber, pageSize);
        return new ResponseEntity<>(houses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> saveHouse(@RequestBody HouseRequestDto house) {
        houseService.saveHouse(house);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> updateHouse(@PathVariable Long id,@RequestBody HouseRequestDto house) {
        houseService.updateHouse(id,house);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHouse(@PathVariable Long id) {
        houseService.deleteHouse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
