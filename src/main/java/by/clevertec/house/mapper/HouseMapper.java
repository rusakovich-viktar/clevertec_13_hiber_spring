package by.clevertec.house.mapper;

import by.clevertec.house.dto.HouseDto;
import by.clevertec.house.entity.HouseEntity;
import by.clevertec.house.entity.PersonEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HouseMapper {
    @Mapping(source = "residents", target = "residentUuids", qualifiedByName = "personToUuid")
    @Mapping(source = "owners", target = "ownerUuids", qualifiedByName = "personToUuid")
    @Mapping(target = "createDateIso", expression = "java(convertToIsoDate(entity.getCreateDate()))")
    HouseDto toDto(HouseEntity entity);

    HouseEntity toEntity(HouseDto dto);

    default String convertToIsoDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    @Named("personToUuid")
    default UUID personToUuid(PersonEntity person) {
        return person.getUuid();
    }
}
