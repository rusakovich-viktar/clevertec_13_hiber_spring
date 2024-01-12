package by.clevertec.house.dao.impl;

import by.clevertec.house.dao.PersonDao;
import by.clevertec.house.entity.PersonEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
@RequiredArgsConstructor
public class PersonDaoImpl implements PersonDao {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public PersonEntity getPersonByUuid(UUID uuid) {
        List<PersonEntity> results = entityManager.createQuery("SELECT p FROM PersonEntity p WHERE p.uuid = :uuid", PersonEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
        if (results.isEmpty()) {
            throw new IllegalArgumentException("Person with UUID " + uuid + " does not exist");
        } else {
            return results.get(0);
        }
    }
    @Override
    public List<PersonEntity> getAllPersons(int pageNumber, int pageSize) {
        return entityManager.createQuery("SELECT p FROM PersonEntity p", PersonEntity.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public void savePerson(PersonEntity person) {
        entityManager.persist(person);
    }

    @Override
    public void updatePerson(PersonEntity person) {
        entityManager.merge(person);
    }

    @Override
    public void deletePerson(UUID uuid) {
        PersonEntity person = getPersonByUuid(uuid);
        if (person != null) {
            entityManager.remove(person);
        }
    }

}
