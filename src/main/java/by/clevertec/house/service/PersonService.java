package by.clevertec.house.service;

import by.clevertec.house.dto.PersonDto;
import java.util.List;

public interface PersonService {
    PersonDto getPersonById(Long id);
    List<PersonDto> getAllPersons();
    void savePerson(PersonDto person);
    void updatePerson(PersonDto person);
    void deletePerson(Long id);
}

