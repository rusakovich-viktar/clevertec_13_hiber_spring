package by.clevertec.house.service;

import by.clevertec.house.dto.HouseDto;
import by.clevertec.house.dto.PersonDto;
import java.util.List;

public interface PersonService {
    PersonDto getPersonById(Long id);

    //    List<PersonDto> getAllPersons();
    List<PersonDto> getAllPersons(int pageNumber, int pageSize);

    void savePerson(PersonDto person);

    void updatePerson(PersonDto person);

    void deletePerson(Long id);

    List<HouseDto> getOwnedHouses(Long personId);

}

