package by.clevertec.house.repository;

import by.clevertec.house.entity.House;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HouseRepository extends JpaRepository<House, UUID> {

    Optional<House> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    List<House> getHousesByOwnersUuid(UUID uuid);

    @Query("SELECT h FROM House h "
            + "JOIN FETCH HouseHistory hh ON h = hh.house "
            + "JOIN FETCH Person p ON hh.person = p "
            + "WHERE p.uuid = :personUuid AND hh.type = 'TENANT'")
    List<House> getPastTenantsByUuid(UUID personUuid);

    @Query("SELECT h FROM House h "
            + "JOIN FETCH HouseHistory hh ON h = hh.house "
            + "JOIN FETCH Person p ON hh.person = p "
            + "WHERE p.uuid = :personUuid AND hh.type = 'OWNER'")
    List<House> findPastOwnedHousesByUuid(UUID personUuid);

}
