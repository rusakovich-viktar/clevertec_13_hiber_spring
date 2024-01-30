package by.clevertec.house.service.impl;

import static java.util.stream.Collectors.toList;

import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.HouseWithHistoryDto;
import by.clevertec.house.dto.PersonRequestDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.entity.House;
import by.clevertec.house.entity.PassportData;
import by.clevertec.house.entity.Person;
import by.clevertec.house.entity.Sex;
import by.clevertec.house.exception.EntityNotFoundException;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.repository.HouseRepository;
import by.clevertec.house.repository.PersonRepository;
import by.clevertec.house.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация сервиса для работы с персонами.
 * Обрабатывает бизнес-логику, связанную с персонами.
 */
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final HouseRepository houseRepository;
    private final PersonMapper personMapper;
    private final HouseMapper houseMapper;

    /**
     * Получает DTO персоны по его UUID.
     *
     * @param uuid UUID персоны.
     * @return DTO персоны.
     */
    @Transactional(readOnly = true)
    @Override
    public PersonResponseDto getPersonByUuid(UUID uuid) {
        Person person = personRepository
                .findByUuid(uuid)
                .orElseThrow(() -> EntityNotFoundException.of(Person.class, uuid));
        return personMapper.toDto(person);
    }

    /**
     * Получает список всех DTO персон с пагинацией.
     *
     * @param pageNumber номер страницы.
     * @param pageSize   размер страницы.
     * @return Список DTO персон.
     */
    @Transactional(readOnly = true)
    @Override
    public List<PersonResponseDto> getAllPersons(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return personRepository.findAll(pageable).stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Сохраняет DTO персоны в базе данных.
     *
     * @param personDto DTO персоны.
     */
    @Transactional
    @Override
    public void savePerson(PersonRequestDto personDto) {
        Person mappedPerson = personMapper.toEntity(personDto);
        if (mappedPerson.getUuid() == null) {
            mappedPerson.setUuid(UUID.randomUUID());
        }
        House houseEntity = returnHouseTenantIfExist(personDto);
        mappedPerson.setHouse(houseEntity);

        Person savedPerson = personRepository.save(mappedPerson);

        List<House> listHouseOwners = getListHouseOwnersIfExist(personDto);
        for (House house : listHouseOwners) {
            house.getOwners().add(savedPerson);
            houseRepository.save(house);
        }

        savedPerson.setOwnedHouses(listHouseOwners);
        personRepository.save(savedPerson);
    }

    /**
     * Обновляет DTO персоны в базе данных по его UUID.
     *
     * @param uuid      UUID персоны.
     * @param personDto DTO персоны с новой информацией.
     */
    @Transactional
    @Override
    public void updatePerson(UUID uuid, @Valid PersonRequestDto personDto) {
        Person existingPerson = personRepository
                .findByUuid(uuid)
                .orElseThrow(() -> EntityNotFoundException.of(Person.class, uuid));

        personMapper.updatePersonDetailsFromDto(existingPerson, personDto);
        updateHouse(existingPerson, personDto);
        updateOwnedHouses(existingPerson, personDto);

        personRepository.save(existingPerson);
    }

    /**
     * Удаляет персону по его UUID из базы данных.
     *
     * @param uuid UUID персоны.
     */
    @Transactional
    @Override
    public void deletePerson(UUID uuid) {
        Person person = personRepository
                .findByUuid(uuid)
                .orElseThrow(() -> EntityNotFoundException.of(Person.class, uuid));

        person.getOwnedHouses().forEach(house -> house.getOwners().remove(person));
        person.getOwnedHouses().clear();

        personRepository.deleteByUuid(uuid);
    }

    /**
     * Обновляет определенные поля персоны по его UUID.
     *
     * @param uuid    UUID персоны.
     * @param updates Map с обновлениями полей.
     */
    @Transactional
    @Override
    public void updatePersonFields(UUID uuid, Map<String, Object> updates) {
        Person existingPerson = personRepository.findByUuid(uuid).orElseThrow(() -> EntityNotFoundException.of(Person.class, uuid));
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
                            existingPerson.setHouse(houseRepository.findByUuid(houseUuid).orElseThrow(() -> EntityNotFoundException.of(House.class, uuid)));
                        }
                    }
                }
            });
        }
        personRepository.save(existingPerson);
    }

    /**
     * Получает список DTO домов, принадлежащих персоне по его UUID.
     *
     * @param personUuid UUID персоны.
     * @return Список DTO домов.
     */
    @Transactional(readOnly = true)
    @Override
    public List<HouseResponseDto> getOwnedHousesByPersonUuid(UUID personUuid) {
        Optional.ofNullable(personUuid).orElseThrow(() -> new IllegalArgumentException("UUID cannot be null"));
        List<House> houses = houseRepository.getHousesByOwnersUuid(personUuid);
        return houses.isEmpty() ? Collections.emptyList() : houses.stream().map(houseMapper::toDto).collect(toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<HouseWithHistoryDto> getPastTenantsByUuid(UUID personUuid) {
        List<Object[]> pastTenants = houseRepository.getPastTenantsByUuid(personUuid);
        return pastTenants.stream()
                .map(obj -> {
                    House house = (House) obj[0];
                    LocalDateTime date = (LocalDateTime) obj[1];
                    return houseMapper.toHouseWithHistoryDto(house, date);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<HouseWithHistoryDto> getPastOwnedHousesByUuid(UUID personUuid) {
        List<Object[]> pastOwnedHouses = houseRepository.findPastOwnedHousesByUuid(personUuid);
        return pastOwnedHouses.stream()
                .map(obj -> {
                    House house = (House) obj[0];
                    LocalDateTime date = (LocalDateTime) obj[1];
                    return houseMapper.toHouseWithHistoryDto(house, date);
                })
                .collect(Collectors.toList());
    }

    /**
     * Обновляет дом персоны на основе DTO.
     *
     * @param person Сущность персоны для обновления.
     * @param dto    DTO с новыми данными персоны.
     */
    private void updateHouse(Person person, PersonRequestDto dto) {
        if (dto.getHouseUuid() != null && !dto.getHouseUuid().equals(person.getHouse().getUuid())) {
            House house = returnHouseTenantIfExist(dto);
            person.setHouse(house);
        }
    }

    /**
     * Обновляет список домов, принадлежащих персоне, на основе DTO.
     *
     * @param person Сущность персоны для обновления.
     * @param dto    DTO с новыми данными персоны.
     */
    private void updateOwnedHouses(Person person, PersonRequestDto dto) {
        if (dto.getOwnedHouseUuids() != null) {
            List<UUID> dtoUuids = new ArrayList<>(dto.getOwnedHouseUuids());
            List<UUID> entityUuids = person.getOwnedHouses().stream()
                    .map(House::getUuid)
                    .distinct()
                    .toList();

            if (!dtoUuids.equals(entityUuids)) {
                List<House> listHouseOwners = getListHouseOwnersIfExist(dto);
                List<House> oldHouses = new ArrayList<>(person.getOwnedHouses());

                person.getOwnedHouses().clear();
                person.getOwnedHouses().addAll(listHouseOwners);

                for (House house : oldHouses) {
                    house.getOwners().remove(person);
                    houseRepository.save(house);
                }

                for (House house : listHouseOwners) {
                    house.getOwners().add(person);
                    houseRepository.save(house);
                }
            }
        }
    }

    /**
     * Получает список сущностей домов, принадлежащих персоне, если они существуют.
     *
     * @param personDto DTO персоны.
     * @return Список сущностей домов.
     */
    private List<House> getListHouseOwnersIfExist(PersonRequestDto personDto) {
        List<UUID> ownedHouseUuids = personDto.getOwnedHouseUuids();
        if (ownedHouseUuids == null || ownedHouseUuids.isEmpty()) {
            return new ArrayList<>();
        }
        return ownedHouseUuids.stream()
                .map(uuid -> houseRepository.findByUuid(uuid).orElseThrow(() -> EntityNotFoundException.of(House.class, uuid)))
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
    private House returnHouseTenantIfExist(PersonRequestDto personDto) {
        UUID houseUuid = personDto.getHouseUuid();
        if (houseUuid == null) {
            throw new IllegalArgumentException("UUID обязателен");
        }
        return houseRepository
                .findByUuid(houseUuid)
                .orElseThrow(() -> EntityNotFoundException.of(House.class, houseUuid));
    }
}
