package by.clevertec.house.dto;

import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HouseRequestDto {
    private UUID uuid;
    private Double area;
    private String country;
    private String city;
    private String street;
    private String number;
    private List<UUID> residentUuids;
    private List<UUID> ownerUuids;
}
