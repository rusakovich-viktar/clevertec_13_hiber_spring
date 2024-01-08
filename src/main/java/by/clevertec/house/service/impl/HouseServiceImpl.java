package by.clevertec.house.service.impl;

import by.clevertec.house.dao.HouseDao;
import by.clevertec.house.dto.HouseDto;
import by.clevertec.house.entity.HouseEntity;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.service.HouseService;
import java.util.List;
import java.util.stream.Collectors;

public class HouseServiceImpl implements HouseService {
    private HouseDao houseDao;
    private HouseMapper houseMapper;

    public HouseServiceImpl(HouseDao houseDao, HouseMapper houseMapper) {
        this.houseDao = houseDao;
        this.houseMapper = houseMapper;
    }

    @Override
    public HouseDto getHouseById(Long id) {
        return houseMapper.toDto(houseDao.getHouseById(id));
    }

    @Override
    public List<HouseDto> getAllHouses() {
        return houseDao.getAllHouses().stream()
                .map(houseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveHouse(HouseDto houseDto) {
        HouseEntity houseEntity = houseMapper.toEntity(houseDto);
        houseDao.saveHouse(houseEntity);
    }

    @Override
    public void updateHouse(HouseDto houseDto) {
        HouseEntity houseEntity = houseMapper.toEntity(houseDto);
        houseDao.updateHouse(houseEntity);
    }

    @Override
    public void deleteHouse(Long id) {
        houseDao.deleteHouse(id);
    }
}

