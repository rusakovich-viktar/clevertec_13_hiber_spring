package by.clevertec.house.dao;

import by.clevertec.house.entity.Person;
import java.util.List;
import java.util.UUID;

public interface PersonDao {

    Person getPersonByUuid(UUID uuid);

    List<Person> getAllPersons(int pageNumber, int pageSize);

    void savePerson(Person person);

    void updatePerson(Person person);

    void deletePerson(UUID uuid);

}
