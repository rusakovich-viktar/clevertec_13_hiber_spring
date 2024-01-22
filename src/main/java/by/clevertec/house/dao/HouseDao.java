package by.clevertec.house.dao;

import by.clevertec.house.entity.House;
import java.util.List;
import java.util.UUID;

public interface HouseDao {

    House getHouseByUuid(UUID uuid);

    List<House> getAllHouses(int pageNumber, int pageSize);

    void saveHouse(House house);

    void updateHouse(House house);

    void deleteHouse(UUID uuid);

    List<House> getHousesByOwnerUuid(UUID uuid);

}
