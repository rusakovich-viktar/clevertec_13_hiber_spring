package by.clevertec.house.dao.impl;

import by.clevertec.house.dao.HouseDao;
import by.clevertec.house.entity.HouseEntity;
import by.clevertec.house.exception.EntityNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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

    public HouseEntity getHouseByUuid(UUID uuid) {
        Optional<HouseEntity> house = Optional.ofNullable(entityManager.createQuery("SELECT h FROM HouseEntity h WHERE h.uuid = :uuid", HouseEntity.class)
                .setParameter("uuid", uuid)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> EntityNotFoundException.of(HouseEntity.class, uuid)));
        return house.get();
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
    public void deleteHouse(UUID uuid) {
        entityManager.createQuery("DELETE FROM HouseEntity h WHERE h.uuid = :uuid")
                .setParameter("uuid", uuid)
                .executeUpdate();
    }


    @Override
    public List<HouseEntity> getHousesByOwnerUuid(UUID uuid) {
        List<HouseEntity> houses = entityManager.createQuery("SELECT h FROM HouseEntity h JOIN h.owners o WHERE o.uuid = :ownerUuid", HouseEntity.class)
                .setParameter("ownerUuid", uuid)
                .getResultList();
        if (houses.isEmpty()) {
            throw new EntityNotFoundException("No houses owned by the person with UUID " + uuid);
        }
        return houses;
    }
}
