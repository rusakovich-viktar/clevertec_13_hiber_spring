package by.clevertec.house.dao.impl;

import by.clevertec.house.dao.PersonDao;
import by.clevertec.house.entity.Person;
import by.clevertec.house.exception.EntityNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация DAO для работы с PersonEntity.
 * Обрабатывает операции с базой данных, связанные с PersonEntity.
 */
@Transactional
@Repository
@RequiredArgsConstructor
public class PersonDaoImpl implements PersonDao {

    @PersistenceContext
    private final EntityManager entityManager;

    /**
     * Получает PersonEntity по его UUID из базы данных.
     *
     * @param uuid UUID PersonEntity.
     * @return PersonEntity.
     * @throws EntityNotFoundException если PersonEntity не найден.
     */
    @Override
    public Person getPersonByUuid(UUID uuid) {
        try {
            return entityManager
                    .createQuery("SELECT p FROM Person p WHERE p.uuid = :uuid", Person.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw EntityNotFoundException.of(Person.class, uuid);
        }
    }

    /**
     * Получает все PersonEntity из базы данных с пагинацией.
     *
     * @param pageNumber номер страницы.
     * @param pageSize   размер страницы.
     * @return Список PersonEntity.
     */
    @Override
    public List<Person> getAllPersons(int pageNumber, int pageSize) {
        return entityManager
                .createQuery("SELECT p FROM Person p", Person.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Сохраняет PersonEntity в базе данных.
     *
     * @param person PersonEntity.
     */
    @Override
    public void savePerson(Person person) {
        entityManager.persist(person);
    }

    /**
     * Обновляет PersonEntity в базе данных.
     *
     * @param person PersonEntity.
     */
    @Override
    public void updatePerson(Person person) {
        entityManager.merge(person);
    }

    /**
     * Удаляет PersonEntity по его UUID из базы данных.
     *
     * @param uuid UUID PersonEntity.
     */
    @Override
    public void deletePerson(UUID uuid) {
        entityManager
                .createQuery("DELETE FROM Person p WHERE p.uuid = :uuid")
                .setParameter("uuid", uuid)
                .executeUpdate();
    }

}
