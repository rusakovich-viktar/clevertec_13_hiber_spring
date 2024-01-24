package by.clevertec.house.dao.impl;

import by.clevertec.house.dao.HouseDao;
import by.clevertec.house.entity.HouseEntity;
import by.clevertec.house.exception.EntityNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация DAO для работы с домами.
 * Обрабатывает операции с базой данных, связанные с домами.
 */
@Transactional
@Repository
@RequiredArgsConstructor
public class HouseDaoImpl implements HouseDao {
    @PersistenceContext
    private final EntityManager entityManager;

    /**
     * Получает дом по его UUID из базы данных.
     *
     * @param uuid UUID дома.
     * @return HouseEntity.
     * @throws EntityNotFoundException если дом не найден.
     */
    public HouseEntity getHouseByUuid(UUID uuid) {
        Optional<HouseEntity> house = Optional.ofNullable(entityManager.createQuery("SELECT h FROM HouseEntity h WHERE h.uuid = :uuid", HouseEntity.class)
                .setParameter("uuid", uuid)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> EntityNotFoundException.of(HouseEntity.class, uuid)));
        return house.get();
    }

    /**
     * Получает все HouseEntity из базы данных с пагинацией.
     *
     * @param pageNumber номер страницы.
     * @param pageSize   размер страницы.
     * @return Список HouseEntity.
     */
    public List<HouseEntity> getAllHouses(int pageNumber, int pageSize) {
        return entityManager.createQuery("SELECT h FROM HouseEntity h", HouseEntity.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Сохраняет HouseEntity в базе данных.
     *
     * @param house HouseEntity.
     */
    @Override
    public void saveHouse(HouseEntity house) {
        entityManager.persist(house);
    }

    /**
     * Обновляет HouseEntity в базе данных.
     *
     * @param house HouseEntity.
     */
    @Override
    public void updateHouse(HouseEntity house) {
        entityManager.merge(house);
    }

    /**
     * Удаляет HouseEntity по его UUID из базы данных.
     *
     * @param uuid UUID HouseEntity.
     */
    @Override
    public void deleteHouse(UUID uuid) {
        entityManager.createQuery("DELETE FROM HouseEntity h WHERE h.uuid = :uuid")
                .setParameter("uuid", uuid)
                .executeUpdate();
    }

    /**
     * Получает HouseEntity, принадлежащие владельцу по его UUID.
     *
     * @param uuid UUID владельца.
     * @return Список HouseEntity.
     * @throws EntityNotFoundException если HouseEntity, принадлежащие владельцу, не найдены.
     */
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
