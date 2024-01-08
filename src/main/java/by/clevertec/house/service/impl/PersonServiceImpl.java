package by.clevertec.house.service.impl;

import by.clevertec.house.dao.PersonDao;
import by.clevertec.house.dto.PersonDto;
import by.clevertec.house.entity.PersonEntity;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.service.PersonService;
import java.util.List;
import java.util.stream.Collectors;

public class PersonServiceImpl implements PersonService {
    private PersonDao personDao;
    private PersonMapper personMapper;

    public PersonServiceImpl(PersonDao personDao, PersonMapper personMapper) {
        this.personDao = personDao;
        this.personMapper = personMapper;
    }

    @Override
    public PersonDto getPersonById(Long id) {
        return personMapper.toDto(personDao.getPersonById(id));
    }

    @Override
    public List<PersonDto> getAllPersons() {
        return personDao.getAllPersons().stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void savePerson(PersonDto personDto) {
        personDao.savePerson(personMapper.toEntity(personDto));
    }

    @Override
    public void updatePerson(PersonDto personDto) {
        PersonEntity existingPerson = personDao.getPersonByUuid(personDto.getUuid());
        PersonEntity newPerson = personMapper.toEntity(personDto);

        if (!existingPerson.equals(newPerson)) {
            personDao.updatePerson(newPerson);
        }
    }

    @Override
    public void deletePerson(Long id) {
        personDao.deletePerson(id);
    }
}
