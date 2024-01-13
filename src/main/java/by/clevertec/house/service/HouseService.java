package by.clevertec.house.service;

import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.PersonResponseDto;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface HouseService {
    HouseResponseDto getHouseByUuid(UUID uuid);

    List<HouseResponseDto> getAllHouses(int pageNumber, int pageSize);

    void saveHouse(HouseRequestDto houseDto);

    void updateHouse(UUID uuid, HouseRequestDto houseDto);

    void deleteHouse(UUID uuid);

    void updateHouseFields(UUID uuid, Map<String, Object> updates);

    List<PersonResponseDto> getResidents(UUID uuid);
}
