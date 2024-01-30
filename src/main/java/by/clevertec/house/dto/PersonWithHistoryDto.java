package by.clevertec.house.dto;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonWithHistoryDto {

    private UUID uuid;
    private String name;
    private String surname;
    private String historyDate;

}
