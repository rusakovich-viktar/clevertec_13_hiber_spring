package by.clevertec.house.dao;

import by.clevertec.house.entity.PersonEntity;
import java.util.List;
import java.util.UUID;

public interface PersonDao {

    PersonEntity getPersonById(Long id);

    List<PersonEntity> getAllPersons();

    void savePerson(PersonEntity person);

    void updatePerson(PersonEntity person);

    void deletePerson(Long id);

    PersonEntity getPersonByUuid(UUID uuid);
}
