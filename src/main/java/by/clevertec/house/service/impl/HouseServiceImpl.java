package by.clevertec.house.service.impl;

import by.clevertec.house.dao.HouseDao;
import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.entity.House;
import by.clevertec.house.exception.EntityNotFoundException;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.service.HouseService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация сервиса для работы с домами.
 * Обрабатывает бизнес-логику, связанную с домами.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    public static final String AREA = "area";
    public static final String COUNTRY = "country";
    public static final String CITY = "city";
    public static final String STREET = "street";
    public static final String NUMBER = "number";
    private final HouseDao houseDao;
    private final HouseMapper houseMapper;
    private final PersonMapper personMapper;
    private final Validator validator;

    /**
     * Получает DTO дома по его UUID.
     *
     * @param uuid UUID дома.
     * @return DTO дома.
     */
    @Override
    public HouseResponseDto getHouseByUuid(UUID uuid) {
        return houseMapper.toDto(houseDao.getHouseByUuid(uuid));
    }

    /**
     * Получает список всех DTO домов с пагинацией.
     *
     * @param pageNumber номер страницы.
     * @param pageSize   размер страницы.
     * @return Список DTO домов.
     */
    @Override
    public List<HouseResponseDto> getAllHouses(int pageNumber, int pageSize) {
        return houseDao.getAllHouses(pageNumber, pageSize).stream()
                .map(houseMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Сохраняет DTO дома в базе данных.
     *
     * @param houseDto DTO дома.
     */
    @Override
    public void saveHouse(HouseRequestDto houseDto) {
        House house = houseMapper.toEntity(houseDto);
        house.setCreateDate(LocalDateTime.now());
        if (house.getUuid() == null) {
            house.setUuid(UUID.randomUUID());
        }
        houseDao.saveHouse(house);
    }

    /**
     * Обновляет DTO дома в базе данных по его UUID.
     *
     * @param uuid     UUID дома.
     * @param houseDto DTO дома с новой информацией.
     */
    @Override
    public void updateHouse(UUID uuid, HouseRequestDto houseDto) {
        Set<ConstraintViolation<HouseRequestDto>> violations = validator.validate(houseDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        House house = houseDao.getHouseByUuid(uuid);

        house.setArea(houseDto.getArea());
        house.setCountry(houseDto.getCountry());
        house.setCity(houseDto.getCity());
        house.setStreet(houseDto.getStreet());
        house.setNumber(houseDto.getNumber());

        houseDao.updateHouse(house);
    }

    /**
     * Удаляет дом по его UUID из базы данных.
     *
     * @param uuid UUID дома.
     */
    @Override
    public void deleteHouse(UUID uuid) {
        houseDao.deleteHouse(uuid);
    }

    /**
     * Обновляет определенные поля дома по его UUID.
     *
     * @param uuid    UUID дома.
     * @param updates Map с обновлениями полей.
     */
    @Override
    public void updateHouseFields(UUID uuid, Map<String, Object> updates) {
        House existingHouse = Optional.ofNullable(houseDao.getHouseByUuid(uuid))
                .orElseThrow(() -> EntityNotFoundException.of(House.class, uuid));
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
        houseDao.updateHouse(existingHouse);
    }

    /**
     * Получает список DTO персон, проживающих в доме по его UUID.
     *
     * @param uuid UUID дома.
     * @return Список DTO персон.
     */
    @Override
    public List<PersonResponseDto> getResidents(UUID uuid) {
        House house = houseDao.getHouseByUuid(uuid);
        return house.getResidents().stream()
                .map(personMapper::toDto)
                .distinct()
                .collect(Collectors.toList());
    }

}
