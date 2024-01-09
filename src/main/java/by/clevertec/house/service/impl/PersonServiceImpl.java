package by.clevertec.house.service.impl;

import by.clevertec.house.dao.HouseDao;
import by.clevertec.house.dao.PersonDao;
import by.clevertec.house.dto.HouseDto;
import by.clevertec.house.dto.PersonDto;
import by.clevertec.house.entity.PersonEntity;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.service.PersonService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
//@Transactional
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonDao personDao;
    private final HouseDao houseDao;
    private final PersonMapper personMapper;
    private final HouseMapper houseMapper;


    @Override
    public PersonDto getPersonById(Long id) {
        return personMapper.toDto(personDao.getPersonById(id));
    }

    @Override
    public List<PersonDto> getAllPersons(int pageNumber, int pageSize) {
        return personDao.getAllPersons(pageNumber, pageSize).stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }
//    @Override
//    public List<PersonDto> getAllPersons() {
//        return personDao.getAllPersons().stream()
//                .map(personMapper::toDto)
//                .collect(Collectors.toList());
//    }

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

    @Override
    public List<HouseDto> getOwnedHouses(Long personId) {
        return houseDao.getHousesByOwnerId(personId).stream()
                .map(houseMapper::toDto)
                .collect(Collectors.toList());
    }
}
