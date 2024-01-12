package by.clevertec.house.service.impl;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import by.clevertec.house.dao.HouseDao;
import by.clevertec.house.dao.PersonDao;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.PersonRequestDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.entity.HouseEntity;
import by.clevertec.house.entity.PersonEntity;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.service.PersonService;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonDao personDao;
    private final HouseDao houseDao;
    private final PersonMapper personMapper;
    private final HouseMapper houseMapper;


    @Override
    public PersonResponseDto getPersonByUuid(UUID uuid) {
        return personMapper.toDto(personDao.getPersonByUuid(uuid));
    }

    @Override
    public List<PersonResponseDto> getAllPersons(int pageNumber, int pageSize) {
        return personDao.getAllPersons(pageNumber, pageSize).stream()
                .map(personMapper::toDto)
                .collect(toList());
    }

    @Override
    public void savePerson(PersonRequestDto personDto) {
        PersonEntity mappedPerson = personMapper.toEntity(personDto);
        trySetHouseAndOwnedHouseToNewPersonFromDto(personDto, mappedPerson);
        personDao.savePerson(mappedPerson);
    }

    @Override
    public void updatePerson(UUID uuid, PersonRequestDto personDto) {
        PersonEntity existingPerson = personDao.getPersonByUuid(uuid);

        if (personDto.getName() != null) {
            existingPerson.setName(personDto.getName());
        }
        if (personDto.getSurname() != null) {
            existingPerson.setSurname(personDto.getSurname());
        }
        if (personDto.getSex() != null) {
            existingPerson.setSex(personDto.getSex());
        }
        if (personDto.getPassportData() != null) {
            existingPerson.setPassportData(personDto.getPassportData());
        }
        if (personDto.getHouseUuid() != null && !personDto.getHouseUuid().equals(existingPerson.getHouse().getUuid())) {
            HouseEntity houseEntity = returnHouseResidentIfExist(personDto);
            existingPerson.setHouse(houseEntity);
        }

        if (personDto.getOwnedHouseUuids() != null) {
            Set<UUID> dtoUuids = new HashSet<>(personDto.getOwnedHouseUuids());
            Set<UUID> entityUuids = existingPerson.getOwnedHouses().stream()
                    .map(HouseEntity::getUuid)
                    .collect(Collectors.toSet());

            if (!dtoUuids.equals(entityUuids)) {

                Set<HouseEntity> listHouseOwners = getListHouseOwnersIfExist(personDto);
                existingPerson.setOwnedHouses(listHouseOwners);

                for (HouseEntity house : listHouseOwners) {
                    house.getOwners().add(existingPerson);
                    houseDao.saveHouse(house);
                }
            }
        }

        personDao.updatePerson(existingPerson);

    }

    @Override
    public void deletePerson(UUID uuid) {
        PersonEntity person = personDao.getPersonByUuid(uuid);
        if (person == null) {
            throw new IllegalArgumentException("Person with UUID " + uuid + " does not exist");
        }
        Iterator<HouseEntity> iterator = person.getOwnedHouses().iterator();
        while (iterator.hasNext()) {
            HouseEntity house = iterator.next();
            house.getOwners().remove(person);
            iterator.remove();
            houseDao.saveHouse(house);
        }
        personDao.deletePerson(uuid);
    }

    @Override
    public List<HouseResponseDto> getOwnedHouses(UUID personUuid) {
        return houseDao.getHousesByOwnerUuid(personUuid).stream()
                .map(houseMapper::toDto)
                .collect(toList());
    }

    private Set<HouseEntity> getListHouseOwnersIfExist(PersonRequestDto personDto) {
        List<UUID> ownedHouseUuids = personDto.getOwnedHouseUuids();
        if (ownedHouseUuids == null || ownedHouseUuids.isEmpty()) {
            return new HashSet<>();
        }
        return ownedHouseUuids.stream()
                .map(houseDao::getHouseByUuid)
                .filter(house -> {
                    if (house == null) {
                        throw new IllegalArgumentException("House with UUID does not exist");
                    }
                    return true;
                })
                .collect(toSet());
    }

    private PersonEntity trySetHouseAndOwnedHouseToNewPersonFromDto(PersonRequestDto personDto, PersonEntity person) {
        HouseEntity houseEntity = returnHouseResidentIfExist(personDto);
        person.setHouse(houseEntity);

        Set<HouseEntity> listHouseOwners = getListHouseOwnersIfExist(personDto);
        person.setOwnedHouses(listHouseOwners);

        for (HouseEntity house : listHouseOwners) {
            house.getOwners().add(person);
            houseDao.saveHouse(house);
        }
        return person;
    }

    private HouseEntity returnHouseResidentIfExist(PersonRequestDto personDto) {
        UUID houseUuid = personDto.getHouseUuid();
        if (houseUuid == null) {
            throw new IllegalArgumentException("UuidHouse обязателен");
        }
        HouseEntity houseResident = houseDao.getHouseByUuid(houseUuid);
        if (houseResident == null) {
            throw new IllegalArgumentException("House with UUID " + houseUuid + " does not exist");
        }
        return houseResident;
    }
}
