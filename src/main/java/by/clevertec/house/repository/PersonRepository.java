package by.clevertec.house.repository;

import by.clevertec.house.entity.Person;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    Optional<Person> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

}
