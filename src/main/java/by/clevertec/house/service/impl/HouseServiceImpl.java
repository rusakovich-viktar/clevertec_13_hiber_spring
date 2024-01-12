package by.clevertec.house.service.impl;

import by.clevertec.house.dao.HouseDao;
import by.clevertec.house.dao.PersonDao;
import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.entity.HouseEntity;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.service.HouseService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {
    private final HouseDao houseDao;
    private final PersonDao personDao;
    private final HouseMapper houseMapper;
    private final PersonMapper personMapper;


    @Override
    public HouseResponseDto getHouseById(Long id) {
        return houseMapper.toDto(houseDao.getHouseById(id));
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
        houseDao.saveHouse(houseEntity);
    }

    @Override
    public void updateHouse(Long id, HouseRequestDto houseDto) {
        HouseEntity houseEntity = houseMapper.toEntity(houseDto);
        houseDao.updateHouse(houseEntity);
        //TODO
    }

    @Override
    public void deleteHouse(Long id) {
        HouseEntity house = houseDao.getHouseById(id);
        if (house == null) {
            throw new IllegalArgumentException("House with ID " + id + " does not exist");
        }
        //Пытается удалить дом, но если там будет жить человек - не дает этого сделать (специально),
        // так как человек не может остаться без жилища.
        houseDao.deleteHouse(id);
    }


    @Override
    public List<PersonResponseDto> getResidents(Long houseId) {
        HouseEntity house = houseDao.getHouseById(houseId);
        return house.getResidents().stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }


}

