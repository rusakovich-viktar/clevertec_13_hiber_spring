package by.clevertec.house.service;

import by.clevertec.house.dto.HouseDto;
import java.util.List;

public interface HouseService {
    HouseDto getHouseById(Long id);
    List<HouseDto> getAllHouses();
    void saveHouse(HouseDto house);
    void updateHouse(HouseDto house);
    void deleteHouse(Long id);
}
