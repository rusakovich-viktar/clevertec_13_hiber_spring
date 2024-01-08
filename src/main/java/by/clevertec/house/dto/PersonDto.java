package by.clevertec.house.dto;

import by.clevertec.house.entity.PassportData;
import by.clevertec.house.entity.Sex;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class PersonDto {
    private UUID uuid;
    private String name;
    private String surname;
    private Sex sex;
    private PassportData passportData;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
