package by.clevertec.house.service.impl;

import by.clevertec.house.dao.HouseDao;
import by.clevertec.house.dto.HouseDto;
import by.clevertec.house.dto.PersonDto;
import by.clevertec.house.entity.HouseEntity;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.service.HouseService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
//@Transactional
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {
    private final HouseDao houseDao;
    private final HouseMapper houseMapper;
    private final PersonMapper personMapper;


    @Override
    public HouseDto getHouseById(Long id) {
        return houseMapper.toDto(houseDao.getHouseById(id));
    }

    //    @Override
//    public List<HouseDto> getAllHouses() {
//        return houseDao.getAllHouses().stream()
//                .map(houseMapper::toDto)
//                .collect(Collectors.toList());
//    }
    @Override
    public List<HouseDto> getAllHouses(int pageNumber, int pageSize) {
        return houseDao.getAllHouses(pageNumber, pageSize).stream()
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

    @Override
    public List<PersonDto> getResidents(Long houseId) {
        HouseEntity house = houseDao.getHouseById(houseId);
        return house.getResidents().stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }
}

