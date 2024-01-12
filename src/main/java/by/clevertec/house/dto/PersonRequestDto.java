package by.clevertec.house.dto;

import by.clevertec.house.entity.PassportData;
import by.clevertec.house.entity.Sex;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class PersonRequestDto {

    private UUID uuid;
    private String name;
    private String surname;
    private Sex sex;
    private PassportData passportData;
    private UUID houseUuid;
    private List<UUID> ownedHouseUuids;

}
