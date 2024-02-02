package by.clevertec.house.util;

import static by.clevertec.house.util.TestConstant.HOUSE_ONE_AREA;
import static by.clevertec.house.util.TestConstant.HOUSE_ONE_CITY;
import static by.clevertec.house.util.TestConstant.HOUSE_ONE_COUNTRY;
import static by.clevertec.house.util.TestConstant.HOUSE_ONE_NUMBER;
import static by.clevertec.house.util.TestConstant.HOUSE_ONE_STREET;
import static by.clevertec.house.util.TestConstant.HOUSE_ONE_UUID;

import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.entity.House;
import by.clevertec.house.entity.Person;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class HouseTestBuilder {

    @Builder.Default
    private UUID uuid = HOUSE_ONE_UUID;

    @Builder.Default
    private double area = HOUSE_ONE_AREA;

    @Builder.Default
    private String country = HOUSE_ONE_COUNTRY;

    @Builder.Default
    private String city = HOUSE_ONE_CITY;

    @Builder.Default
    private String street = HOUSE_ONE_STREET;

    @Builder.Default
    private String number = HOUSE_ONE_NUMBER;

    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.of(2024, 1, 30, 10, 0, 0);

    @Builder.Default
    private List<Person> tenants = new ArrayList<>();

    @Builder.Default
    private Set<Person> owners = new HashSet<>();

    @Builder.Default
    private List<UUID> tenantsUuids = new ArrayList<>();

    @Builder.Default
    private List<UUID> ownersUuids = new ArrayList<>();

    public House buildHouse() {
        House house = new House();
        house.setUuid(uuid);
        house.setArea(area);
        house.setCountry(country);
        house.setCity(city);
        house.setStreet(street);
        house.setNumber(number);
        house.setCreateDate(createDate);
        house.setTenants(tenants);
        house.setOwners(owners);
        return house;
    }

    public HouseResponseDto buildHouseResponseDto() {
        House house = this.buildHouse();
        HouseResponseDto houseResponseDto = new HouseResponseDto();
        houseResponseDto.setUuid(house.getUuid());
        houseResponseDto.setArea(house.getArea());
        houseResponseDto.setCountry(house.getCountry());
        houseResponseDto.setCity(house.getCity());
        houseResponseDto.setStreet(house.getStreet());
        houseResponseDto.setNumber(house.getNumber());
        houseResponseDto.setCreateDate(house.getCreateDate().toString());
        return houseResponseDto;
    }

    public HouseRequestDto buildHouseRequestDto() {
        HouseRequestDto houseRequestDto = new HouseRequestDto();
        houseRequestDto.setArea(area);
        houseRequestDto.setCountry(country);
        houseRequestDto.setCity(city);
        houseRequestDto.setStreet(street);
        houseRequestDto.setNumber(number);
        houseRequestDto.setTenantUuids(tenantsUuids);
        houseRequestDto.setOwnerUuids(ownersUuids);
        return houseRequestDto;
    }
}
