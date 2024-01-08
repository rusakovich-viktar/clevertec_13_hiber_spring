package by.clevertec.house.dao.impl;

import by.clevertec.house.dao.HouseDao;
import by.clevertec.house.entity.HouseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

public class HouseDaoImpl implements HouseDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public HouseEntity getHouseById(Long id) {
        return entityManager.find(HouseEntity.class, id);
    }

    @Override
    public List<HouseEntity> getAllHouses() {
        return entityManager.createQuery("SELECT h FROM HouseEntity h", HouseEntity.class).getResultList();
    }

    @Override
    public void saveHouse(HouseEntity house) {
        entityManager.persist(house);
    }

    @Override
    public void updateHouse(HouseEntity house) {
        entityManager.merge(house);
    }

    @Override
    public void deleteHouse(Long id) {
        HouseEntity house = getHouseById(id);
        if (house != null) {
            entityManager.remove(house);
        }
    }
}
