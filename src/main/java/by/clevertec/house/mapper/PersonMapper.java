package by.clevertec.house.mapper;

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
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PersonMapper {

    @Mapping(target = "createDate", expression = "java(convertToIsoDate(entity.getCreateDate()))")
    @Mapping(target = "updateDate", expression = "java(convertToIsoDate(entity.getUpdateDate()))")
    PersonResponseDto toDto(Person entity);

    Person toEntity(PersonRequestDto dto);

    default String convertToIsoDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    default PersonWithHistoryDto toPersonWithHistoryDto(Person person, LocalDateTime date) {
        PersonWithHistoryDto dto = new PersonWithHistoryDto();
        dto.setName(person.getName());
        dto.setSurname(person.getSurname());
        dto.setUuid(person.getUuid());
        dto.setHistoryDate(date.format(DateTimeFormatter.ISO_DATE_TIME));
        return dto;
    }

    default PassportData toPassportData(PassportDataDto dto) {
        PassportData data = new PassportData();
        data.setPassportSeries(dto.getPassportSeries());
        data.setPassportNumber(dto.getPassportNumber());
        return data;
    }

    default void updatePersonDetailsFromDto(Person person, PersonRequestDto dto) {
        person.setName(dto.getName());
        person.setSurname(dto.getSurname());
        person.setSex(dto.getSex());
        person.setPassportData(toPassportData(dto.getPassportData()));
    }
}
