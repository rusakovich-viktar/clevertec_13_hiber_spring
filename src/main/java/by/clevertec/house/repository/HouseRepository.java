package by.clevertec.house.repository;

import by.clevertec.house.entity.House;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HouseRepository extends JpaRepository<House, UUID> {

    Optional<House> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    @Query("SELECT h FROM House h JOIN h.owners p WHERE p.uuid = :uuid")
    List<House> getOwnedHousesByPersonUuid(@Param("uuid") UUID uuid);

    @Query("SELECT h, hh.date FROM House h "
            + "JOIN FETCH HouseHistory hh ON h.id = hh.houseId "
            + "JOIN FETCH Person p ON hh.personId = p.id "
            + "WHERE p.uuid = :personUuid AND hh.type = 'TENANT'")
    List<Object[]> getPastTenantsByUuid(UUID personUuid);

    @Query("SELECT h, hh.date FROM House h "
            + "JOIN FETCH HouseHistory hh ON h.id = hh.houseId "
            + "JOIN FETCH Person p ON hh.personId = p.id "
            + "WHERE p.uuid = :personUuid AND hh.type = 'OWNER'")
    List<Object[]> findPastOwnedHousesByUuid(UUID personUuid);

}
