package by.clevertec.house.service.impl;

import by.clevertec.house.dao.HouseDao;
import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.entity.HouseEntity;
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


    @Override
    public HouseResponseDto getHouseByUuid(UUID uuid) {
        return houseMapper.toDto(houseDao.getHouseByUuid(uuid));
    }

    @Override
    public List<HouseResponseDto> getAllHouses(int pageNumber, int pageSize) {
        return houseDao.getAllHouses(pageNumber, pageSize).stream()
                .map(houseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveHouse(HouseRequestDto houseDto) {
        HouseEntity houseEntity = houseMapper.toEntity(houseDto);
        houseEntity.setCreateDate(LocalDateTime.now());
        if (houseEntity.getUuid() == null) {
            houseEntity.setUuid(UUID.randomUUID());
        }
        houseDao.saveHouse(houseEntity);
    }

    @Override
    public void updateHouse(UUID uuid, HouseRequestDto houseDto) {
        Set<ConstraintViolation<HouseRequestDto>> violations = validator.validate(houseDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        HouseEntity houseEntity = houseDao.getHouseByUuid(uuid);

        houseEntity.setArea(houseDto.getArea());
        houseEntity.setCountry(houseDto.getCountry());
        houseEntity.setCity(houseDto.getCity());
        houseEntity.setStreet(houseDto.getStreet());
        houseEntity.setNumber(houseDto.getNumber());

        houseDao.updateHouse(houseEntity);
    }

    @Override
    public void deleteHouse(UUID uuid) {
        houseDao.deleteHouse(uuid);
    }

    @Override
    public void updateHouseFields(UUID uuid, Map<String, Object> updates) {
        HouseEntity existingHouse = Optional.ofNullable(houseDao.getHouseByUuid(uuid))
                .orElseThrow(() -> EntityNotFoundException.of(HouseEntity.class, uuid));
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

    @Override
    public List<PersonResponseDto> getResidents(UUID uuid) {
        HouseEntity house = houseDao.getHouseByUuid(uuid);
        return house.getResidents().stream()
                .map(personMapper::toDto)
                .distinct()
                .collect(Collectors.toList());
    }

}
