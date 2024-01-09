package by.clevertec.house.service;

import by.clevertec.house.dto.HouseDto;
import by.clevertec.house.dto.PersonDto;
import java.util.List;

public interface HouseService {
    HouseDto getHouseById(Long id);

    //    List<HouseDto> getAllHouses();
    List<HouseDto> getAllHouses(int pageNumber, int pageSize);

    void saveHouse(HouseDto house);

    void updateHouse(HouseDto house);

    void deleteHouse(Long id);

    List<PersonDto> getResidents(Long houseId);
}
