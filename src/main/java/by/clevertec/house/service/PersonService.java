package by.clevertec.house.service;

import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.PersonRequestDto;
import by.clevertec.house.dto.PersonResponseDto;
import java.util.List;
import java.util.UUID;

public interface PersonService {
    PersonResponseDto getPersonByUuid(UUID uuid);

    List<PersonResponseDto> getAllPersons(int pageNumber, int pageSize);

    void savePerson(PersonRequestDto person);

    void updatePerson(UUID uuid, PersonRequestDto person);

    void deletePerson(UUID uuid);

    List<HouseResponseDto> getOwnedHouses(UUID uuid);

}

