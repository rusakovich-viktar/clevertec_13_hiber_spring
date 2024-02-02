package by.clevertec.house.mapper;

import static by.clevertec.house.util.TestConstant.PERSON_ONE_CREATE_DATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.HouseWithHistoryDto;
import by.clevertec.house.entity.House;
import by.clevertec.house.entity.House.Fields;
import by.clevertec.house.util.HouseTestBuilder;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class HouseMapperTest {

    private final HouseMapper houseMapper = Mappers.getMapper(HouseMapper.class);

    @Test
    void testToDtoShouldReturnHouseResponseDto() {
        // given
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();
        HouseResponseDto expected = HouseTestBuilder.builder()
                .build()
                .buildHouseResponseDto();

        // when
        HouseResponseDto actual = houseMapper.toDto(house);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(House.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(House.Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(House.Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(House.Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(House.Fields.street, expected.getStreet())
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber())
                .hasFieldOrPropertyWithValue(House.Fields.createDate, expected.getCreateDate());
    }

    @Test
    void testToDtoShouldReturnNullWhenEntityIsNull() {
        // when
        HouseResponseDto actual = houseMapper.toDto(null);

        // then
        assertNull(actual);
    }

    @Test
    void testToEntityShouldReturnHouse() {
        // given
        HouseRequestDto dto = HouseTestBuilder.builder()
                .build()
                .buildHouseRequestDto();
        House expected = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        // when
        House actual = houseMapper.toEntity(dto);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(HouseWithHistoryDto.Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(HouseWithHistoryDto.Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(HouseWithHistoryDto.Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(HouseWithHistoryDto.Fields.street, expected.getStreet())
                .hasFieldOrPropertyWithValue(HouseWithHistoryDto.Fields.number, expected.getNumber());
    }

    @Test
    void testToEntityShouldReturnNullWhenDtoIsNull() {
        // when
        House actual = houseMapper.toEntity(null);

        // then
        assertNull(actual);
    }

    @Test
    void testToHouseWithHistoryDtoShouldReturnHouseWithHistoryDto() {
        // given
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();
        LocalDateTime date = PERSON_ONE_CREATE_DATE;
        HouseWithHistoryDto expected = HouseTestBuilder.builder()
                .withHistoryDate(date.toString())
                .build()
                .buildHouseWithHistoryDto();

        // when
        HouseWithHistoryDto actual = houseMapper.toHouseWithHistoryDto(house, date);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(HouseWithHistoryDto.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(HouseWithHistoryDto.Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(HouseWithHistoryDto.Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(HouseWithHistoryDto.Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(HouseWithHistoryDto.Fields.street, expected.getStreet())
                .hasFieldOrPropertyWithValue(HouseWithHistoryDto.Fields.number, expected.getNumber())
                .hasFieldOrPropertyWithValue(HouseWithHistoryDto.Fields.historyDate, expected.getHistoryDate());

    }

    @Test
    void testToHouseWithHistoryDtoShouldReturnNullWhenHouseAndDateAreNull() {
        // when
        HouseWithHistoryDto actual = houseMapper.toHouseWithHistoryDto(null, null);

        // then
        assertNull(actual);
    }

    @Test
    void testUpdateHouseDetailsFromDtoShouldUpdateHouseDetails() {
        // given
        House actual = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        HouseRequestDto expected = HouseTestBuilder.builder()
                .build()
                .buildHouseRequestDto();

        // when
        houseMapper.updateHouseFromDto(expected, actual);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(Fields.street, expected.getStreet())
                .hasFieldOrPropertyWithValue(Fields.number, expected.getNumber());
    }

    @Test
    void testConvertToIsoDateShouldReturnIsoFormattedString() {
        // given
        LocalDateTime date = LocalDateTime.of(2023, 6, 30, 23, 59, 59);

        // when
        String isoDate = houseMapper.convertToIsoDate(date);

        // then
        assertEquals("2023-06-30T23:59:59", isoDate);
    }
}
