package by.clevertec.house.service.impl;

import static java.util.stream.Collectors.toList;

import by.clevertec.house.dao.HouseDao;
import by.clevertec.house.dao.PersonDao;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.PersonRequestDto;
import by.clevertec.house.dto.PersonRequestDto.PassportDataDto;
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
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация сервиса для работы с персонами.
 * Обрабатывает бизнес-логику, связанную с персонами.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonDao personDao;
    private final HouseDao houseDao;
    private final PersonMapper personMapper;
    private final HouseMapper houseMapper;

    /**
     * Получает DTO персоны по его UUID.
     *
     * @param uuid UUID персоны.
     * @return DTO персоны.
     */
    @Override
    public PersonResponseDto getPersonByUuid(UUID uuid) {
        return personMapper.toDto(personDao.getPersonByUuid(uuid));
    }

    /**
     * Получает список всех DTO персон с пагинацией.
     *
     * @param pageNumber номер страницы.
     * @param pageSize   размер страницы.
     * @return Список DTO персон.
     */
    @Override
    public List<PersonResponseDto> getAllPersons(int pageNumber, int pageSize) {
        return personDao.getAllPersons(pageNumber, pageSize).stream()
                .map(personMapper::toDto)
                .collect(toList());
    }

    /**
     * Сохраняет DTO персоны в базе данных.
     *
     * @param personDto DTO персоны.
     */
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

    /**
     * Обновляет DTO персоны в базе данных по его UUID.
     *
     * @param uuid      UUID персоны.
     * @param personDto DTO персоны с новой информацией.
     */
    @Override
    public void updatePerson(UUID uuid, @Valid PersonRequestDto personDto) {
        PersonEntity existingPerson = personDao.getPersonByUuid(uuid);

        updatePersonDetails(existingPerson, personDto);
        updateHouse(existingPerson, personDto);
        updateOwnedHouses(existingPerson, personDto);

        personDao.updatePerson(existingPerson);
    }

    /**
     * Удаляет персону по его UUID из базы данных.
     *
     * @param uuid UUID персоны.
     */
    @Override
    public void deletePerson(UUID uuid) {
        PersonEntity person = personDao.getPersonByUuid(uuid);
        if (person == null) {
            throw EntityNotFoundException.of(PersonEntity.class, uuid);
        }

        person.getOwnedHouses().forEach(house -> house.getOwners().remove(person));
        person.getOwnedHouses().clear();

        personDao.deletePerson(uuid);
    }

    /**
     * Обновляет определенные поля персоны по его UUID.
     *
     * @param uuid    UUID персоны.
     * @param updates Map с обновлениями полей.
     */
    public void updatePersonFields(UUID uuid, Map<String, Object> updates) {
        PersonEntity existingPerson = personDao.getPersonByUuid(uuid);
        ObjectMapper mapper = new ObjectMapper();
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            Optional.ofNullable(entry.getValue()).ifPresent(value -> {
                switch (entry.getKey()) {
                    case "name" -> existingPerson.setName((String) value);
                    case "surname" -> existingPerson.setSurname((String) value);
                    case "sex" -> existingPerson.setSex(Sex.valueOf((String) value));
                    case "passportData" ->
                            existingPerson.setPassportData(mapper.convertValue(value, PassportData.class));
                    case "houseUuid" -> {
                        UUID houseUuid = UUID.fromString((String) value);
                        if (!houseUuid.equals(existingPerson.getHouse().getUuid())) {
                            existingPerson.setHouse(houseDao.getHouseByUuid(houseUuid));
                        }
                    }
                }
            });
        }
        personDao.updatePerson(existingPerson);
    }

    /**
     * Получает список DTO домов, принадлежащих персоне по его UUID.
     *
     * @param personUuid UUID персоны.
     * @return Список DTO домов.
     */
    @Override
    public List<HouseResponseDto> getOwnedHouses(UUID personUuid) {
        Optional.ofNullable(personUuid).orElseThrow(() -> new IllegalArgumentException("UUID cannot be null"));
        List<HouseEntity> houses = houseDao.getHousesByOwnerUuid(personUuid);
        return houses.isEmpty() ? Collections.emptyList() : houses.stream().map(houseMapper::toDto).collect(toList());
    }

    /**
     * Обновляет детали персоны на основе DTO.
     *
     * @param person Сущность персоны для обновления.
     * @param dto    DTO с новыми данными персоны.
     */
    private void updatePersonDetails(PersonEntity person, PersonRequestDto dto) {
        person.setName(dto.getName());
        person.setSurname(dto.getSurname());
        person.setSex(dto.getSex());
        person.setPassportData(convertToPassportData(dto.getPassportData()));
    }

    /**
     * Обновляет дом персоны на основе DTO.
     *
     * @param person Сущность персоны для обновления.
     * @param dto    DTO с новыми данными персоны.
     */
    private void updateHouse(PersonEntity person, PersonRequestDto dto) {
        if (dto.getHouseUuid() != null && !dto.getHouseUuid().equals(person.getHouse().getUuid())) {
            HouseEntity houseEntity = returnHouseResidentIfExist(dto);
            person.setHouse(houseEntity);
        }
    }

    /**
     * Обновляет список домов, принадлежащих персоне, на основе DTO.
     *
     * @param person Сущность персоны для обновления.
     * @param dto    DTO с новыми данными персоны.
     */
    private void updateOwnedHouses(PersonEntity person, PersonRequestDto dto) {
        if (dto.getOwnedHouseUuids() != null) {
            List<UUID> dtoUuids = new ArrayList<>(dto.getOwnedHouseUuids());
            List<UUID> entityUuids = person.getOwnedHouses().stream()
                    .map(HouseEntity::getUuid)
                    .distinct()
                    .toList();

            if (!dtoUuids.equals(entityUuids)) {
                List<HouseEntity> listHouseOwners = getListHouseOwnersIfExist(dto);
                person.setOwnedHouses(listHouseOwners);

                for (HouseEntity house : listHouseOwners) {
                    house.getOwners().add(person);
                    houseDao.saveHouse(house);
                }
            }
        }
    }

    /**
     * Конвертирует DTO данных паспорта в сущность данных паспорта.
     *
     * @param dto DTO данных паспорта.
     * @return Сущность данных паспорта.
     */
    private PassportData convertToPassportData(PassportDataDto dto) {
        PassportData data = new PassportData();
        data.setPassportSeries(dto.getPassportSeries());
        data.setPassportNumber(dto.getPassportNumber());
        return data;
    }

    /**
     * Получает список сущностей домов, принадлежащих персоне, если они существуют.
     *
     * @param personDto DTO персоны.
     * @return Список сущностей домов.
     */
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

    /**
     * Возвращает сущность дома, в котором проживает персона, если она существует.
     *
     * @param personDto DTO персоны.
     * @return Сущность дома.
     * @throws IllegalArgumentException если UUID отсутствует.
     * @throws EntityNotFoundException  если дом не найден.
     */
    private HouseEntity returnHouseResidentIfExist(PersonRequestDto personDto) {
        UUID houseUuid = personDto.getHouseUuid();
        if (houseUuid == null) {
            throw new IllegalArgumentException("UUID обязателен");
        }
        HouseEntity houseResident = houseDao.getHouseByUuid(houseUuid);
        if (houseResident == null) {
            throw EntityNotFoundException.of(HouseEntity.class, houseUuid);
        }
        return houseResident;
    }
}
