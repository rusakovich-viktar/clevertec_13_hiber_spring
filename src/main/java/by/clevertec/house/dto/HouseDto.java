package by.clevertec.house.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class HouseDto {
    private UUID uuid;
    private double area;
    private String country;
    private String city;
    private String street;
    private String number;
    private LocalDateTime createDate;
}
