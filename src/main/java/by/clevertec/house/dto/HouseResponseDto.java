package by.clevertec.house.dto;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HouseResponseDto {

    private UUID uuid;
    private double area;
    private String country;
    private String city;
    private String street;
    private String number;
    private String createDateIso;
}
