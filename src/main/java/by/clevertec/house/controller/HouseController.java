package by.clevertec.house.controller;

import by.clevertec.house.dto.HouseDto;
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
    public ResponseEntity<HouseDto> getHouseById(@PathVariable Long id) {
        HouseDto house = houseService.getHouseById(id);
        return new ResponseEntity<>(house, HttpStatus.OK);

    }

    //    @GetMapping
//    public ResponseEntity<List<HouseDto>> getAllHouses() {
//        List<HouseDto> houses = houseService.getAllHouses();
//        return new ResponseEntity<>(houses, HttpStatus.OK);
//    }
    @GetMapping
    public ResponseEntity<List<HouseDto>> getAllHouses(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "15") int pageSize) {
        List<HouseDto> houses = houseService.getAllHouses(pageNumber, pageSize);
        return new ResponseEntity<>(houses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> saveHouse(@RequestBody HouseDto house) {
        houseService.saveHouse(house);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> updateHouse(@RequestBody HouseDto house) {
        houseService.updateHouse(house);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHouse(@PathVariable Long id) {
        houseService.deleteHouse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
