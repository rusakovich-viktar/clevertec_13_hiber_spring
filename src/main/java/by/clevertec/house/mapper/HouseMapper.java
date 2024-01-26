package by.clevertec.house.mapper;

import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.HouseWithHistoryDto;
import by.clevertec.house.entity.House;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HouseMapper {

    @Mapping(target = "createDate", expression = "java(convertToIsoDate(entity.getCreateDate()))")
    HouseResponseDto toDto(House entity);

    House toEntity(HouseRequestDto dto);

    default String convertToIsoDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    default void updateHouseFromDto(HouseRequestDto dto, House house) {
        house.setArea(dto.getArea());
        house.setCountry(dto.getCountry());
        house.setCity(dto.getCity());
        house.setStreet(dto.getStreet());
        house.setNumber(dto.getNumber());
    }

    default HouseWithHistoryDto toHouseWithHistoryDto(House house, LocalDateTime date) {
        HouseWithHistoryDto dto = new HouseWithHistoryDto();
        dto.setUuid(house.getUuid());
        dto.setArea(house.getArea());
        dto.setCountry(house.getCountry());
        dto.setCity(house.getCity());
        dto.setStreet(house.getStreet());
        dto.setNumber(house.getNumber());
        dto.setHistoryDate(date.format(DateTimeFormatter.ISO_DATE_TIME));
        return dto;
    }
}
