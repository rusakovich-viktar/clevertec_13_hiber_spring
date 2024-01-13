package by.clevertec.house.service.impl;

import static java.util.stream.Collectors.toList;

import by.clevertec.house.dao.HouseDao;
import by.clevertec.house.dao.PersonDao;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.PersonRequestDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.entity.HouseEntity;
import by.clevertec.house.entity.PassportData;
import by.clevertec.house.entity.PersonEntity;
import by.clevertec.house.entity.Sex;
import by.clevertec.house.exception.EntityNotFoundException;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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
        if (mappedPerson.getUuid() == null) {
            mappedPerson.setUuid(UUID.randomUUID());
        }
        HouseEntity houseEntity = returnHouseResidentIfExist(personDto);
        mappedPerson.setHouse(houseEntity);

        List<HouseEntity> listHouseOwners = getListHouseOwnersIfExist(personDto);
        mappedPerson.setOwnedHouses(listHouseOwners);

        for (HouseEntity house : listHouseOwners) {
            house.getOwners().add(mappedPerson);
            houseDao.saveHouse(house);
        }

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
            List<UUID> dtoUuids = new ArrayList<>(personDto.getOwnedHouseUuids());
            List<UUID> entityUuids = existingPerson.getOwnedHouses().stream()
                    .map(HouseEntity::getUuid)
                    .distinct()
                    .toList();

            if (!dtoUuids.equals(entityUuids)) {

                List<HouseEntity> listHouseOwners = getListHouseOwnersIfExist(personDto);
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

        Optional<PersonEntity> optionalPerson = Optional.ofNullable(personDao.getPersonByUuid(uuid));
        PersonEntity person = optionalPerson.orElseThrow(() -> EntityNotFoundException.of(PersonEntity.class, uuid));


        person.getOwnedHouses().forEach(house -> {
            house.getOwners().remove(person);
            houseDao.saveHouse(house);
        });
        person.getOwnedHouses().clear();

        personDao.deletePerson(uuid);
    }

    public void updatePersonFields(UUID uuid, Map<String, Object> updates) {
        PersonEntity existingPerson = personDao.getPersonByUuid(uuid);
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            if (entry.getValue() != null) {
                switch (entry.getKey()) {
                    case "name" -> existingPerson.setName((String) entry.getValue());
                    case "surname" -> existingPerson.setSurname((String) entry.getValue());
                    case "sex" -> existingPerson.setSex(Sex.valueOf((String) entry.getValue()));
                    case "passportData" -> {
                        PassportData newPassportData = new ObjectMapper().convertValue(entry.getValue(), PassportData.class);
                        existingPerson.setPassportData(newPassportData);
                    }
                    case "houseUuid" -> {
                        UUID houseUuid = UUID.fromString((String) entry.getValue());
                        if (!houseUuid.equals(existingPerson.getHouse().getUuid())) {
                            HouseEntity houseEntity = houseDao.getHouseByUuid(houseUuid);
                            existingPerson.setHouse(houseEntity);
                        }
                    }
                }
            }
        }
        personDao.updatePerson(existingPerson);
    }

    @Override
    public List<HouseResponseDto> getOwnedHouses(UUID personUuid) {
        if (personUuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
        List<HouseEntity> houses = houseDao.getHousesByOwnerUuid(personUuid);
        if (houses.isEmpty()) {
            throw new EntityNotFoundException("No houses owned by the person with UUID " + personUuid);
        }
        return houses.stream().map(houseMapper::toDto).collect(toList());
    }

    private List<HouseEntity> getListHouseOwnersIfExist(PersonRequestDto personDto) {
        List<UUID> ownedHouseUuids = personDto.getOwnedHouseUuids();
        if (ownedHouseUuids == null || ownedHouseUuids.isEmpty()) {
            return new ArrayList<>();
        }
        return ownedHouseUuids.stream()
                .map(uuid -> {
                    HouseEntity house = houseDao.getHouseByUuid(uuid);
                    if (house == null) {
                        throw EntityNotFoundException.of(HouseEntity.class, uuid);
                    }
                    return house;
                })
                .distinct()
                .collect(toList());
    }

    private HouseEntity returnHouseResidentIfExist(PersonRequestDto personDto) {
        UUID houseUuid = personDto.getHouseUuid();
        if (houseUuid == null) {
            throw new IllegalArgumentException("UuidHouse обязателен");
        }
        HouseEntity houseResident = houseDao.getHouseByUuid(houseUuid);
        if (houseResident == null) {
            throw EntityNotFoundException.of(HouseEntity.class, houseUuid);
        }
        return houseResident;
    }
}
