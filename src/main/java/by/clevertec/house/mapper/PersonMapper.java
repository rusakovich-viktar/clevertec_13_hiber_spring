package by.clevertec.house.mapper;

import by.clevertec.house.dto.PersonRequestDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.entity.PersonEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PersonMapper {

    @Mapping(target = "createDateIso", expression = "java(convertToIsoDate(entity.getCreateDate()))")
    @Mapping(target = "updateDateIso", expression = "java(convertToIsoDate(entity.getUpdateDate()))")
    PersonResponseDto toDto(PersonEntity entity);

    PersonEntity toEntity(PersonRequestDto dto);

    default String convertToIsoDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ISO_DATE_TIME);
    }

}
