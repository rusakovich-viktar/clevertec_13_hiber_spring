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
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HouseMapper {

    @Mapping(target = "createDate", expression = "java(convertToIsoDate(entity.getCreateDate()))")
    HouseResponseDto toDto(House entity);

    House toEntity(HouseRequestDto dto);

    void updateHouseFromDto(HouseRequestDto dto, @MappingTarget House house);

    @Mapping(target = "historyDate", expression = "java(convertToIsoDate(date))")
    HouseWithHistoryDto toHouseWithHistoryDto(House house, LocalDateTime date);

    default String convertToIsoDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
