package by.clevertec.house.dto;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@NoArgsConstructor
@FieldNameConstants
public class HouseWithHistoryDto {

    private UUID uuid;
    private double area;
    private String country;
    private String city;
    private String street;
    private String number;
    private String historyDate;
}
