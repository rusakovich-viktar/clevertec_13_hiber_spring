package by.clevertec.house.dao.impl;

import by.clevertec.house.dao.HouseDao;
import by.clevertec.house.entity.HouseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
@RequiredArgsConstructor
public class HouseDaoImpl implements HouseDao {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public HouseEntity getHouseById(Long id) {
        return entityManager.find(HouseEntity.class, id);

    }

    public  HouseEntity getHouseByUuid(UUID uuid){
        List<HouseEntity> results = entityManager.createQuery("SELECT h FROM HouseEntity h WHERE h.uuid = :uuid", HouseEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
        return results.stream().findFirst().orElse(null);
    }


    public List<HouseEntity> getAllHouses(int pageNumber, int pageSize) {
        return entityManager.createQuery("SELECT h FROM HouseEntity h", HouseEntity.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
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

    @Override
    public List<HouseEntity> getHousesByOwnerUuid(UUID uuid) {
        return entityManager.createQuery("SELECT h FROM HouseEntity h JOIN h.owners o WHERE o.id = :ownerId", HouseEntity.class)
                .setParameter("ownerUuid", uuid)
                .getResultList();
    }
}
