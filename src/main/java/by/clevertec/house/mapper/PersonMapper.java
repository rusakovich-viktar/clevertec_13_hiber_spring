package by.clevertec.house.mapper;

import static by.clevertec.house.util.Constant.Attributes.ISO_DATE_TIME;

import by.clevertec.house.dto.PersonRequestDto;
import by.clevertec.house.dto.PersonRequestDto.PassportDataDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.dto.PersonWithHistoryDto;
import by.clevertec.house.entity.PassportData;
import by.clevertec.house.entity.Person;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PersonMapper {

    @Mapping(target = "createDate", dateFormat = ISO_DATE_TIME)
    @Mapping(target = "updateDate", dateFormat = ISO_DATE_TIME)
    PersonResponseDto toDto(Person entity);

    @Mapping(target = "passportData", source = "passportData")
    Person toEntity(PersonRequestDto dto);

    @Mapping(target = "historyDate", expression = "java(convertToIsoDate(date))")
    PersonWithHistoryDto toPersonWithHistoryDto(Person person, LocalDateTime date);

    PassportData toPassportData(PassportDataDto dto);

    void updatePersonDetailsFromDto(@MappingTarget Person person, PersonRequestDto dto);

    default String convertToIsoDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
