package by.clevertec.house.repository;

import by.clevertec.house.entity.House;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<House, UUID> {

    Optional<House> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    List<House> getHousesByOwnersUuid(UUID uuid);

}
