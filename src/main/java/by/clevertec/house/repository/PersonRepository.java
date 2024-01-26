package by.clevertec.house.repository;

import by.clevertec.house.entity.Person;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    Optional<Person> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    @Query("SELECT p FROM Person p "
            + "JOIN FETCH House h ON p.house = h "
            + "JOIN FETCH HouseHistory hh ON h = hh.house "
            + "WHERE h.uuid = :uuid AND hh.type = 'TENANT'")
    List<Person> findPastTenantsByHouseUuid(UUID uuid);

    @Query("SELECT p FROM Person p "
            + "JOIN FETCH House h ON p.house = h "
            + "JOIN FETCH HouseHistory hh ON h = hh.house "
            + "WHERE h.uuid = :uuid AND hh.type = 'OWNER'")
    List<Person> findPastOwnersByHouseUuid(UUID uuid);

}
