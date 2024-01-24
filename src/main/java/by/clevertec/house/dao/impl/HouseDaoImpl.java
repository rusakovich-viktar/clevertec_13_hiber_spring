package by.clevertec.house.dao.impl;

import by.clevertec.house.dao.HouseDao;
import by.clevertec.house.entity.House;
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
    @Override
    public House getHouseByUuid(UUID uuid) {
        Optional<House> house = Optional.ofNullable(entityManager
                .createQuery("SELECT h FROM House h WHERE h.uuid = :uuid", House.class)
                .setParameter("uuid", uuid)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> EntityNotFoundException.of(House.class, uuid)));
        return house.get();
    }

    /**
     * Получает все HouseEntity из базы данных с пагинацией.
     *
     * @param pageNumber номер страницы.
     * @param pageSize   размер страницы.
     * @return Список HouseEntity.
     */
    @Override
    public List<House> getAllHouses(int pageNumber, int pageSize) {
        return entityManager
                .createQuery("SELECT h FROM House h", House.class)
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
    public void saveHouse(House house) {
        entityManager.persist(house);
    }

    /**
     * Обновляет HouseEntity в базе данных.
     *
     * @param house HouseEntity.
     */
    @Override
    public void updateHouse(House house) {
        entityManager.merge(house);
    }

    /**
     * Удаляет HouseEntity по его UUID из базы данных.
     *
     * @param uuid UUID HouseEntity.
     */
    @Override
    public void deleteHouse(UUID uuid) {
        entityManager
                .createQuery("DELETE FROM House h WHERE h.uuid = :uuid")
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
    public List<House> getHousesByOwnerUuid(UUID uuid) {
        List<House> houses = entityManager
                .createQuery("SELECT h FROM House h JOIN h.owners o WHERE o.uuid = :ownerUuid", House.class)
                .setParameter("ownerUuid", uuid)
                .getResultList();
        if (houses.isEmpty()) {
            throw new EntityNotFoundException("No houses owned by the person with UUID " + uuid);
        }
        return houses;
    }
}
