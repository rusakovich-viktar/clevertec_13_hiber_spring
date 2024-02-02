package by.clevertec.house.util;

import by.clevertec.house.dto.PersonRequestDto.PassportDataDto;
import by.clevertec.house.entity.PassportData;
import lombok.Builder;


@Builder(setterPrefix = "with")
public class PassportTestBuilder {

    @Builder.Default
    private String passportSeries = TestConstant.PERSON_ONE_PASSPORT_SERIES;

    @Builder.Default
    private String passportNumber = TestConstant.PERSON_ONE_PASSPORT_NUMBER;

    public PassportData buildPassport() {
        return new PassportData(passportSeries, passportNumber);
    }

    public PassportDataDto buildPassportDto() {
        return new PassportDataDto(passportSeries, passportNumber);
    }
}
