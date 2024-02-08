package by.clevertec.house.service.impl;

import static by.clevertec.house.util.Constant.Attributes.AREA;
import static by.clevertec.house.util.Constant.Attributes.CITY;
import static by.clevertec.house.util.Constant.Attributes.COUNTRY;
import static by.clevertec.house.util.Constant.Attributes.NUMBER;
import static by.clevertec.house.util.Constant.Attributes.STREET;
import static by.clevertec.house.util.Constant.Attributes.VALUE_ONE_TO_SIMPLIFY_UI;

import by.clevertec.exception.EntityNotFoundException;
import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.dto.PersonWithHistoryDto;
import by.clevertec.house.entity.House;
import by.clevertec.house.entity.Person;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.proxy.annotation.Cacheable;
import by.clevertec.house.repository.HouseRepository;
import by.clevertec.house.repository.PersonRepository;
import by.clevertec.house.service.HouseService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация сервиса для работы с домами.
 * Обрабатывает бизнес-логику, связанную с домами.
 */
@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;

    private final PersonRepository personRepository;
    private final HouseMapper houseMapper;
    private final PersonMapper personMapper;
    private final Validator validator;

    /**
     * Получает DTO дома по его UUID.
     *
     * @param uuid UUID дома.
     * @return DTO дома.
     */

    @Transactional(readOnly = true)
    @Cacheable
    @Override
    public HouseResponseDto getHouseByUuid(UUID uuid) {
        House house = houseRepository
                .findByUuid(uuid)
                .orElseThrow(() -> EntityNotFoundException.of(House.class, uuid));
        return houseMapper.toDto(house);
    }

    /**
     * Получает список всех DTO домов с пагинацией.
     *
     * @param pageNumber номер страницы.
     * @param pageSize   размер страницы.
     * @return Список DTO домов.
     */

    @Transactional(readOnly = true)
    @Override
    public List<HouseResponseDto> getAllHouses(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - VALUE_ONE_TO_SIMPLIFY_UI, pageSize);
        return houseRepository.findAll(pageable).stream()
                .map(houseMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Сохраняет DTO дома в базе данных.
     *
     * @param houseDto DTO дома.
     */

    @Transactional
    @Cacheable
    @Override
    public void saveHouse(HouseRequestDto houseDto) {
        House house = houseMapper.toEntity(houseDto);
        house.setCreateDate(LocalDateTime.now());
        houseRepository.save(house);
    }

    /**
     * Обновляет DTO дома в базе данных по его UUID.
     *
     * @param uuid     UUID дома.
     * @param houseDto DTO дома с новой информацией.
     */

    @Transactional
    @Cacheable
    @Override
    public void updateHouse(UUID uuid, HouseRequestDto houseDto) {
        Set<ConstraintViolation<HouseRequestDto>> violations = validator.validate(houseDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        House house = houseRepository
                .findByUuid(uuid)
                .orElseThrow(() -> EntityNotFoundException.of(House.class, uuid));

        houseMapper.updateHouseFromDto(houseDto, house);

        houseRepository.save(house);
    }

    /**
     * Удаляет дом по его UUID из базы данных.
     *
     * @param uuid UUID дома.
     */

    @Transactional
    @Cacheable
    @Override
    public void deleteHouse(UUID uuid) {
        houseRepository.deleteByUuid(uuid);
    }

    /**
     * Обновляет определенные поля дома по его UUID.
     *
     * @param uuid    UUID дома.
     * @param updates Map с обновлениями полей.
     */
    @Transactional
    @Override
    public void updateHouseFields(UUID uuid, Map<String, Object> updates) {
        House existingHouse = houseRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("House not found"));
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            if (entry.getValue() != null) {
                switch (entry.getKey()) {
                    case AREA -> existingHouse.setArea(Double.parseDouble(entry.getValue().toString()));
                    case COUNTRY -> existingHouse.setCountry((String) entry.getValue());
                    case CITY -> existingHouse.setCity((String) entry.getValue());
                    case STREET -> existingHouse.setStreet((String) entry.getValue());
                    case NUMBER -> existingHouse.setNumber((String) entry.getValue());
                }
            }
        }
        houseRepository.save(existingHouse);
    }

    /**
     * Получает список DTO персон, проживающих в доме по его UUID.
     *
     * @param uuid UUID дома.
     * @return Список DTO персон.
     */
    @Transactional(readOnly = true)
    @Override
    public List<PersonResponseDto> getPersonsWhoLiveHereNowByHouseUuid(UUID uuid) {
        House house = houseRepository.findByUuid(uuid).orElseThrow(() ->
                EntityNotFoundException.of(House.class, uuid));
        return house.getTenants().stream()
                .map(personMapper::toDto)
                .distinct()
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<PersonWithHistoryDto> getPersonsWithHistoryWhoLivedHereInPastByHouseUuid(UUID uuid) {
        List<Object[]> pastTenants = personRepository.findPastTenantsByHouseUuid(uuid);
        return getCollectObjectListToPersonWithHistoryDtoList(pastTenants);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonWithHistoryDto> getPersonsWithHistoryWhoOwnedThisHouseByHouseUuid(UUID uuid) {
        List<Object[]> pastOwners = personRepository.findPastOwnersByHouseUuid(uuid);
        return getCollectObjectListToPersonWithHistoryDtoList(pastOwners);
    }

    private List<PersonWithHistoryDto> getCollectObjectListToPersonWithHistoryDtoList(List<Object[]> pastTenants) {
        return pastTenants.stream()
                .map(obj -> {
                    Person person = (Person) obj[0];
                    LocalDateTime date = (LocalDateTime) obj[1];
                    PersonWithHistoryDto personWithHistoryDto = personMapper.toPersonWithHistoryDto(person, date);
                    return personWithHistoryDto;
                })
                .collect(Collectors.toList());
    }

}
