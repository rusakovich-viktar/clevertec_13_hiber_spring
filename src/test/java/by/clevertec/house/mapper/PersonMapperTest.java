package by.clevertec.house.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import by.clevertec.house.dto.PersonRequestDto;
import by.clevertec.house.dto.PersonRequestDto.PassportDataDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.dto.PersonWithHistoryDto;
import by.clevertec.house.entity.PassportData;
import by.clevertec.house.entity.Person;
import by.clevertec.house.entity.Person.Fields;
import by.clevertec.house.entity.Sex;
import by.clevertec.house.util.PassportTestBuilder;
import by.clevertec.house.util.PersonTestBuilder;
import by.clevertec.house.util.TestConstant;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class PersonMapperTest {

    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);

    @Test
    void testToDtoShouldReturnPersonResponseDto() {
        // given
        Person person = PersonTestBuilder.builder()
                .build()
                .buildPerson();
        PersonResponseDto expected = PersonTestBuilder.builder()
                .build()
                .buildPersonResponseDto();

        // when
        PersonResponseDto actual = personMapper.toDto(person);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Person.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(Fields.sex, expected.getSex())
                .hasFieldOrPropertyWithValue(Fields.createDate, expected.getCreateDate())
                .hasFieldOrPropertyWithValue(Fields.updateDate, expected.getUpdateDate());

        assertThat(actual.getPassportData().getPassportSeries())
                .isEqualTo(expected.getPassportData().getPassportSeries());
        assertThat(actual.getPassportData().getPassportNumber())
                .isEqualTo(expected.getPassportData().getPassportNumber());

    }

    @Test
    void testToDtoShouldReturnNull_whenEntityIsNull() {
        // when
        PersonResponseDto actual = personMapper.toDto(null);

        // then
        assertNull(actual);
    }

    @Test
    void testToEntityShouldReturnPerson() {
        // given
        PersonRequestDto dto = PersonTestBuilder.builder()
                .build()
                .buildPersonRequestDto();
        Person expected = PersonTestBuilder.builder()
                .build()
                .buildPerson();

        // when
        Person actual = personMapper.toEntity(dto);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Person.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(Fields.sex, expected.getSex());

        assertThat(actual.getPassportData().getPassportSeries())
                .isEqualTo(expected.getPassportData().getPassportSeries());
        assertThat(actual.getPassportData().getPassportNumber())
                .isEqualTo(expected.getPassportData().getPassportNumber());
    }

    @Test
    void testToEntityShouldReturnNull_whenDtoIsNull() {
        // when
        Person actual = personMapper.toEntity(null);

        // then
        assertNull(actual);
    }

    @Test
    void testToPersonWithHistoryDtoShouldReturnPersonWithHistoryDto() {
        // given
        Person person = PersonTestBuilder.builder()
                .build()
                .buildPerson();
        LocalDateTime date = TestConstant.PERSON_ONE_CREATE_DATE;
        PersonWithHistoryDto expected = PersonTestBuilder.builder()
                .withHistoryDate(date.toString())
                .build()
                .buildPersonWithHistoryDto();

        // when
        PersonWithHistoryDto actual = personMapper.toPersonWithHistoryDto(person, date);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(PersonWithHistoryDto.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(PersonWithHistoryDto.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(PersonWithHistoryDto.Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(PersonWithHistoryDto.Fields.historyDate, expected.getHistoryDate());
    }

    @Test
    void testToPersonWithHistoryDtoShouldReturnNull_whenPersonAndDateAreNull() {
        // when
        PersonWithHistoryDto actual = personMapper.toPersonWithHistoryDto(null, null);

        // then
        assertNull(actual);
    }

    @Test
    void testUpdatePersonDetailsFromDtoShouldUpdatePersonDetails() {
        // given
        Person actual = PersonTestBuilder.builder()
                .build()
                .buildPerson();

        PersonRequestDto expected = PersonTestBuilder.builder()
                .withName("New Name")
                .withSurname("New Surname")
                .withPassportData(new PassportData("EE", "2222222"))
                .withSex(Sex.FEMALE)
                .build()
                .buildPersonRequestDto();

        // when
        personMapper.updatePersonDetailsFromDto(actual, expected);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(Fields.sex, expected.getSex());

        assertThat(actual.getPassportData().getPassportSeries())
                .isEqualTo(expected.getPassportData().getPassportSeries());
        assertThat(actual.getPassportData().getPassportNumber())
                .isEqualTo(expected.getPassportData().getPassportNumber());
    }

    @Test
    void testToPassportDataShouldReturnNull_whenDtoIsNull() {
        // when
        PassportData actual = personMapper.toPassportData(null);

        // then
        assertNull(actual);
    }

    @Test
    void testPassportDataDtoShouldReturnPassportData() {
        // given
        PassportDataDto expected = PassportTestBuilder.builder()
                .build()
                .buildPassportDto();

        // when
        PassportData actual = personMapper.toPassportData(expected);

        // then
        assertEquals(expected.getPassportSeries(), actual.getPassportSeries());
        assertEquals(expected.getPassportNumber(), actual.getPassportNumber());
    }

    @Test
    void testConvertToIsoDateShouldReturnIsoFormattedString() {
        // given
        LocalDateTime date = LocalDateTime.of(2023, 6, 30, 23, 59, 59);

        // when
        String isoDate = personMapper.convertToIsoDate(date);

        // then
        assertEquals("2023-06-30T23:59:59", isoDate);
    }

}
