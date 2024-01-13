package by.clevertec.house.dao;

import by.clevertec.house.entity.HouseEntity;
import java.util.List;
import java.util.UUID;

public interface HouseDao {
    HouseEntity getHouseByUuid(UUID uuid);

    List<HouseEntity> getAllHouses(int pageNumber, int pageSize);

    void saveHouse(HouseEntity house);

    void updateHouse(HouseEntity house);

    void deleteHouse(UUID uuid);

    List<HouseEntity> getHousesByOwnerUuid(UUID uuid);

}
