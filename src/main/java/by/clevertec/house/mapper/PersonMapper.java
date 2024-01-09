package by.clevertec.house.mapper;

import by.clevertec.house.dto.PersonDto;
import by.clevertec.house.entity.HouseEntity;
import by.clevertec.house.entity.PersonEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PersonMapper {
    @Mapping(source = "house.uuid", target = "houseUuid")
    @Mapping(source = "ownedHouses", target = "ownedHouseUuids", qualifiedByName = "houseToUuid")
    @Mapping(target = "createDateIso", expression = "java(convertToIsoDate(entity.getCreateDate()))")
    @Mapping(target = "updateDateIso", expression = "java(convertToIsoDate(entity.getUpdateDate()))")
    PersonDto toDto(PersonEntity entity);

    PersonEntity toEntity(PersonDto dto);

    default String convertToIsoDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    @Named("houseToUuid")
    default List<UUID> houseToUuid(List<HouseEntity> houses) {
        if (houses == null) {
            return null;
        }
        List<UUID> uuids = new ArrayList<>();
        for (HouseEntity house : houses) {
            uuids.add(house.getUuid());
        }
        return uuids;
    }
}
