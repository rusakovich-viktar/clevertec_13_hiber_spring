package by.clevertec.house.dao.impl;

import by.clevertec.house.dao.PersonDao;
import by.clevertec.house.entity.PersonEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

public class PersonDaoImpl implements PersonDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PersonEntity getPersonById(Long id) {
        return entityManager.find(PersonEntity.class, id);
    }

    @Override
    public List<PersonEntity> getAllPersons() {
        return entityManager.createQuery("SELECT p FROM PersonEntity p", PersonEntity.class).getResultList();
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
    public void deletePerson(Long id) {
        PersonEntity person = getPersonById(id);
        if (person != null) {
            entityManager.remove(person);
        }
    }

    @Override
    public PersonEntity getPersonByUuid(UUID uuid) {
        return entityManager.find(PersonEntity.class, uuid);
    }
}
