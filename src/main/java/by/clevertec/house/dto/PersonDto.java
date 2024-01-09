package by.clevertec.house.dto;

import by.clevertec.house.entity.Sex;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonDto {
    private UUID uuid;
    private String name;
    private String surname;
    private Sex sex;
    private String passportSeries;
    private String passportNumber;
    //    private PassportData passportData;
    private String createDateIso;
    private String updateDateIso;
    private UUID houseUuid;
    private List<UUID> ownedHouseUuids;
}