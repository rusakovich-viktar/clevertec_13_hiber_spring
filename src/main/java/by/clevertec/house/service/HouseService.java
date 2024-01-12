package by.clevertec.house.service;

import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.PersonResponseDto;
import java.util.List;

public interface HouseService {
    HouseResponseDto getHouseById(Long id);

    List<HouseResponseDto> getAllHouses(int pageNumber, int pageSize);

    void saveHouse(HouseRequestDto houseDto);

    void updateHouse(Long id, HouseRequestDto houseDto);

    void deleteHouse(Long id);

    List<PersonResponseDto> getResidents(Long houseId);
}
