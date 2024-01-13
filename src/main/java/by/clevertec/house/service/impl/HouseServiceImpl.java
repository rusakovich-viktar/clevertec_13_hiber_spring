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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    private final HouseDao houseDao;
    private final HouseMapper houseMapper;
    private final PersonMapper personMapper;


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
        HouseEntity houseEntity = houseDao.getHouseByUuid(uuid);

        Map<String, Object> fields = new HashMap<>();
        fields.put("area", houseDto.getArea());
        fields.put("country", houseDto.getCountry());
        fields.put("city", houseDto.getCity());
        fields.put("street", houseDto.getStreet());
        fields.put("number", houseDto.getNumber());

        for (Map.Entry<String, Object> field : fields.entrySet()) {
            if (field.getValue() == null) {
                throw new IllegalArgumentException("Поле '" + field.getKey() + "' не может быть null");
            }
        }

        houseEntity.setArea(houseDto.getArea());
        houseEntity.setCountry(houseDto.getCountry());
        houseEntity.setCity(houseDto.getCity());
        houseEntity.setStreet(houseDto.getStreet());
        houseEntity.setNumber(houseDto.getNumber());

        houseDao.updateHouse(houseEntity);
    }


    @Override
    public void deleteHouse(UUID uuid) {
        HouseEntity house = houseDao.getHouseByUuid(uuid);
        //Пытается удалить дом, но если там будет жить человек - не дает этого сделать (специально),
        // так как человек не может остаться без жилища.
        houseDao.deleteHouse(uuid);
    }

    @Override
    public void updateHouseFields(UUID uuid, Map<String, Object> updates) {
        HouseEntity existingHouse = houseDao.getHouseByUuid(uuid);
        if (existingHouse == null) {
            throw EntityNotFoundException.of(HouseEntity.class, uuid);
        }
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            if (entry.getValue() != null) {
                switch (entry.getKey()) {
                    case "area" -> existingHouse.setArea(Double.parseDouble(entry.getValue().toString()));
                    case "country" -> existingHouse.setCountry((String) entry.getValue());
                    case "city" -> existingHouse.setCity((String) entry.getValue());
                    case "street" -> existingHouse.setStreet((String) entry.getValue());
                    case "number" -> existingHouse.setNumber((String) entry.getValue());
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

