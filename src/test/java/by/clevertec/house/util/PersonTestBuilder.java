package by.clevertec.house.util;

import static by.clevertec.house.util.TestConstant.HOUSE_ONE_UUID;
import static by.clevertec.house.util.TestConstant.PERSON_ONE_CREATE_DATE;
import static by.clevertec.house.util.TestConstant.PERSON_ONE_NAME;
import static by.clevertec.house.util.TestConstant.PERSON_ONE_SURNAME;
import static by.clevertec.house.util.TestConstant.PERSON_ONE_UPDATE_DATE;
import static by.clevertec.house.util.TestConstant.PERSON_ONE_UUID;

import by.clevertec.house.dto.PersonRequestDto;
import by.clevertec.house.dto.PersonRequestDto.PassportDataDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.dto.PersonWithHistoryDto;
import by.clevertec.house.entity.House;
import by.clevertec.house.entity.PassportData;
import by.clevertec.house.entity.Person;
import by.clevertec.house.entity.Sex;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class PersonTestBuilder {

    @Builder.Default
    private UUID uuid = PERSON_ONE_UUID;

    @Builder.Default
    private String name = PERSON_ONE_NAME;

    @Builder.Default
    private String surname = PERSON_ONE_SURNAME;

    @Builder.Default
    private Sex sex = Sex.MALE;

    @Builder.Default
    private PassportData passportData = PassportTestBuilder.builder()
            .build()
            .buildPassport();

    @Builder.Default
    private PassportDataDto passportDataDto = PassportTestBuilder.builder()
            .build()
            .buildPassportDto();
    @Builder.Default
    private House house = HouseTestBuilder.builder().build().buildHouse();

    @Builder.Default
    private List<House> ownedHouses = new ArrayList<>();

    @Builder.Default
    private LocalDateTime createDate = PERSON_ONE_CREATE_DATE;

    @Builder.Default
    private LocalDateTime updateDate = PERSON_ONE_UPDATE_DATE;

    public PersonTestBuilder house(House house) {
        this.house = house;
        return this;
    }

    public Person buildPerson() {
        Person person = new Person();
        person.setName(name);
        person.setSurname(surname);
        person.setSex(sex);
        person.setPassportData(passportData);
        person.setHouse(house);
        person.setOwnedHouses(ownedHouses);
        person.setUuid(uuid);
        person.setCreateDate(createDate);
        person.setUpdateDate(createDate);
        return person;
    }

    public PersonResponseDto buildPersonResponseDto() {
        Person person = this.buildPerson();
        PersonResponseDto personResponseDto = new PersonResponseDto();
        personResponseDto.setUuid(person.getUuid());
        personResponseDto.setName(person.getName());
        personResponseDto.setSurname(person.getSurname());
        personResponseDto.setSex(person.getSex());
        personResponseDto.setPassportData(person.getPassportData());
        personResponseDto.setCreateDate(person.getCreateDate().toString());
        personResponseDto.setUpdateDate(person.getUpdateDate().toString());
        return personResponseDto;
    }

    public PersonWithHistoryDto buildPersonWithHistoryDto() {
        PersonWithHistoryDto personWithHistoryDto = new PersonWithHistoryDto();
        personWithHistoryDto.setHistoryDate(personWithHistoryDto.getHistoryDate());
        personWithHistoryDto.setName(personWithHistoryDto.getName());
        personWithHistoryDto.setSurname(personWithHistoryDto.getSurname());
        personWithHistoryDto.setUuid(personWithHistoryDto.getUuid());
        return personWithHistoryDto;
    }

    public PersonRequestDto buildPersonRequestDto() {
        PersonRequestDto personRequestDto = new PersonRequestDto();
        personRequestDto.setUuid(uuid);
        personRequestDto.setName(name);
        personRequestDto.setSurname(surname);
        personRequestDto.setSex(sex);
        personRequestDto.setPassportData(passportDataDto);
        personRequestDto.setHouseUuid(TestConstant.HOUSE_ONE_UUID);
        personRequestDto.setOwnedHouseUuids(List.of(HOUSE_ONE_UUID));
        return personRequestDto;
    }
}
