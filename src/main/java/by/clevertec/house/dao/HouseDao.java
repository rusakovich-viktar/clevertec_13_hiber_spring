package by.clevertec.house.dao;

import by.clevertec.house.entity.HouseEntity;
import java.util.List;

public interface HouseDao {
    HouseEntity getHouseById(Long id);
    List<HouseEntity> getAllHouses();
    void saveHouse(HouseEntity house);
    void updateHouse(HouseEntity house);
    void deleteHouse(Long id);
}
