package by.clevertec.house.service.impl;

import static by.clevertec.house.util.Constant.Attributes.VALUE_ONE_TO_SIMPLIFY_UI;
import static by.clevertec.house.util.TestConstant.ALEX;
import static by.clevertec.house.util.TestConstant.ALEXEEY;
import static by.clevertec.house.util.TestConstant.PAGE_NUMBER_SHOULD_BE_POSITIVE;
import static by.clevertec.house.util.TestConstant.PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.HouseWithHistoryDto;
import by.clevertec.house.dto.PersonRequestDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.entity.House;
import by.clevertec.house.entity.PassportData;
import by.clevertec.house.entity.Person;
import by.clevertec.house.entity.Sex;
import by.clevertec.house.exception.EntityNotFoundException;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.repository.HouseRepository;
import by.clevertec.house.repository.PersonRepository;
import by.clevertec.house.util.Constant.Attributes;
import by.clevertec.house.util.HouseTestBuilder;
import by.clevertec.house.util.PersonTestBuilder;
import by.clevertec.house.util.TestConstant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private PersonMapper personMapper;

    @Mock
    private HouseMapper houseMapper;

    @InjectMocks
    private PersonServiceImpl personService;

    @Captor
    private ArgumentCaptor<Person> personCaptor;

    UUID randomUuid = UUID.randomUUID();

    @Test
    void testGetPersonByUuidShouldReturnPerson_whenItExist() {
        // given
        Person person = PersonTestBuilder.builder()
                .build()
                .buildPerson();
        PersonResponseDto expected = PersonTestBuilder.builder()
                .build()
                .buildPersonResponseDto();
        UUID uuid = person.getUuid();

        // when
        when(personRepository.findByUuid(uuid))
                .thenReturn(Optional.of(person));
        when(personMapper.toDto(person))
                .thenReturn(expected);

        PersonResponseDto actual = personService.getPersonByUuid(uuid);

        // then
        verify(personMapper)
                .toDto(person);
        assertEquals(expected, actual);
    }

    @Test
    void testGetAllPersonsShouldReturnAllPersons() {
        // given
        Person person = PersonTestBuilder.builder()
                .build()
                .buildPerson();
        PersonResponseDto expectedPersonDto = PersonTestBuilder.builder()
                .build()
                .buildPersonResponseDto();
        Page<Person> page = new PageImpl<>(List.of(person));

        // when
        when(personRepository.findAll(PageRequest
                .of(PAGE_NUMBER_SHOULD_BE_POSITIVE - VALUE_ONE_TO_SIMPLIFY_UI, PAGE_SIZE)))
                .thenReturn(page);
        when(personMapper.toDto(person))
                .thenReturn(expectedPersonDto);

        List<PersonResponseDto> actualPersons = personService
                .getAllPersons(PAGE_NUMBER_SHOULD_BE_POSITIVE, PAGE_SIZE);

        // then
        assertThat(actualPersons)
                .containsOnly(expectedPersonDto);
    }

    @Test
    void testSavePersonShouldSaveCorrectPerson_whenMappedPersonUuidNotnull() {
        // given
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();
        PersonRequestDto personDto = PersonTestBuilder.builder()
                .build()
                .buildPersonRequestDto();
        Person expectedPerson = PersonTestBuilder.builder()
                .build()
                .buildPerson();

        when(houseRepository.findByUuid(house.getUuid()))
                .thenReturn(Optional.of(house));
        when(personMapper.toEntity(personDto))
                .thenReturn(expectedPerson);
        when(personRepository.save(argThat
                (person -> person.getName().equals(expectedPerson.getName())
                        && person.getSurname().equals(expectedPerson.getSurname()))))
                .thenReturn(expectedPerson);

        // when
        personService.savePerson(personDto);

        // then
        verify(personRepository, times(2))
                .save(personCaptor.capture());

        Person savedPerson = personCaptor.getValue();
        assertEquals(expectedPerson, savedPerson);
    }

    @Test
    void testSavePersonShouldSaveCorrectPerson_whenMappedPersonUuidIsNull() {
        // given
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();
        PersonRequestDto personDto = PersonTestBuilder.builder()
                .build()
                .buildPersonRequestDto();
        Person expectedPerson = PersonTestBuilder.builder()
                .withUuid(null)
                .build()
                .buildPerson();

        when(houseRepository.findByUuid(house.getUuid()))
                .thenReturn(Optional.of(house));
        when(personMapper.toEntity(personDto))
                .thenReturn(expectedPerson);
        when(personRepository.save(argThat
                (person -> person.getName().equals(expectedPerson.getName())
                        && person.getSurname().equals(expectedPerson.getSurname()))))
                .thenReturn(expectedPerson);

        // when
        personService.savePerson(personDto);

        // then
        verify(personRepository, times(2))
                .save(personCaptor.capture());


        Person savedPerson = personCaptor.getValue();
        assertNotNull(savedPerson.getUuid());
        assertEquals(expectedPerson, savedPerson);
    }

    @Test
    void testUpdatePersonShouldUpdateCorrectPerson() {
        // given
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        PersonRequestDto personDto = PersonTestBuilder.builder()
                .build()
                .buildPersonRequestDto();
        Person existingPerson = PersonTestBuilder.builder()
                .build()
                .buildPerson();

        when(houseRepository.findByUuid(house.getUuid()))
                .thenReturn(Optional.of(house));
        when(personRepository.findByUuid(existingPerson.getUuid()))
                .thenReturn(Optional.of(existingPerson));

        // when
        personService.updatePerson(existingPerson.getUuid(), personDto);

        // then
        verify(personMapper)
                .updatePersonDetailsFromDto(existingPerson, personDto);
        verify(personRepository)
                .save(existingPerson);
    }

    @Test
    void testUpdatePersonShouldThrowException_whenInvalidUuid() {
        // given
        PersonRequestDto personDto = PersonTestBuilder.builder()
                .build()
                .buildPersonRequestDto();

        when(personRepository.findByUuid(randomUuid))
                .thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () ->
                personService.updatePerson(randomUuid, personDto));
    }

    @Test
    void testUpdatePersonShouldUpdateHouse_whenDtoHouseUuidIsNotNullAndDifferent() {
        // given
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();
        PersonRequestDto personDto = PersonTestBuilder.builder()
                .build()
                .buildPersonRequestDto();
        Person existingPerson = PersonTestBuilder.builder()
                .withHouse(new House())
                .build()
                .buildPerson();

        when(houseRepository.findByUuid(house.getUuid()))
                .thenReturn(Optional.of(house));
        when(personRepository.findByUuid(existingPerson.getUuid()))
                .thenReturn(Optional.of(existingPerson));

        // when
        personService.updatePerson(existingPerson.getUuid(), personDto);

        // then
        assertEquals(house, existingPerson.getHouse());
    }

    @Test
    void testDeletePersonShouldTryDeleteEnteredPerson() {
        // given
        Person person = PersonTestBuilder.builder().build().buildPerson();

        when(personRepository.findByUuid(person.getUuid()))
                .thenReturn(Optional.of(person));

        // when
        personService.deletePerson(person.getUuid());

        // then
        verify(personRepository)
                .deleteByUuid(person.getUuid());
    }

    @Test
    void testUpdatePersonFieldsShouldUpdateCorrectPerson() {
        // given
        UUID houseUuid = UUID.randomUUID();
        Map<String, Object> updates = new HashMap<>();
        updates.put(Attributes.NAME, ALEX);
        updates.put(Attributes.SURNAME, ALEXEEY);
        updates.put(Attributes.SEX, "MALE");
        updates.put(Attributes.PASSPORT_DATA, new PassportData());
        updates.put(Attributes.HOUSE_UUID, houseUuid.toString());

        House house = HouseTestBuilder.builder().build().buildHouse();
        Person existingPerson = PersonTestBuilder.builder()
                .withHouse(house)
                .build()
                .buildPerson();

        when(personRepository.findByUuid(existingPerson.getUuid()))
                .thenReturn(Optional.of(existingPerson));
        when(houseRepository.findByUuid(houseUuid))
                .thenReturn(Optional.of(house));

        // when
        personService.updatePersonFields(existingPerson.getUuid(), updates);

        // then
        verify(personRepository)
                .findByUuid(existingPerson.getUuid());
        verify(personRepository)
                .save(personCaptor.capture());
        Person savedPerson = personCaptor.getValue();

        assertEquals(ALEX, savedPerson.getName());
        assertEquals(ALEXEEY, savedPerson.getSurname());
        assertEquals(Sex.MALE, savedPerson.getSex());
        assertNotNull(savedPerson.getPassportData());
        assertEquals(house, savedPerson.getHouse());
        verify(personRepository)
                .save(existingPerson);
    }

    @Test
    void testGetOwnedHousesByPersonUuidShouldReturnCorrectList_whenItExist() {
        // given
        House house = HouseTestBuilder.builder().build().buildHouse();
        Person person = PersonTestBuilder.builder().build().buildPerson();
        person.setOwnedHouses(List.of(house));

        when(houseRepository.getOwnedHousesByPersonUuid(person.getUuid()))
                .thenReturn(List.of(house));
        when(houseMapper.toDto(house)).thenReturn(new HouseResponseDto());

        // when
        List<HouseResponseDto> actual = personService.getOwnedHousesByPersonUuid(person.getUuid());

        // then
        assertEquals(1, actual.size());
        verify(houseRepository)
                .getOwnedHousesByPersonUuid(person.getUuid());
    }

    @Test
    void testGetOwnedHousesByPersonUuidShouldReturnEmptyListWhenNoHouses() {
        // given
        UUID personUuid = UUID.randomUUID();
        when(houseRepository.getOwnedHousesByPersonUuid(personUuid))
                .thenReturn(Collections.emptyList());

        // when
        List<HouseResponseDto> result = personService.getOwnedHousesByPersonUuid(personUuid);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetTenantedHousesHistoryByPersonUuidShouldReturnListHouseWithHistoryDto() {

        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        LocalDateTime date = TestConstant.PERSON_ONE_CREATE_DATE;

        HouseWithHistoryDto houseWithHistoryDto = HouseTestBuilder.builder()
                .withHistoryDate(date.toString())
                .build()
                .buildHouseWithHistoryDto();

        List<Object[]> pastTenants = new ArrayList<>();
        pastTenants.add(new Object[]{house, date});

        Mockito.when(houseRepository.getPastTenantedHousesByPersonUuid(house.getUuid()))
                .thenReturn(pastTenants);
        when(houseMapper.toHouseWithHistoryDto(house, date))
                .thenReturn(houseWithHistoryDto);

        List<HouseWithHistoryDto> result = personService.getTenantedHousesHistoryByPersonUuid(house.getUuid());

        assertNotNull(result);
        assertEquals(1, result.size());

        HouseWithHistoryDto dto = result.get(0);
        assertNotNull(dto); // Проверяем, что dto не null

        Assertions.assertEquals(house.getUuid(), dto.getUuid());
        Assertions.assertEquals(date.toString(), dto.getHistoryDate());
    }

    @Test
    void testGetOwnedHousesHistoryByPersonUuidShouldReturnListHouseWithHistoryDto() {

        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        LocalDateTime date = TestConstant.PERSON_ONE_CREATE_DATE;

        HouseWithHistoryDto houseWithHistoryDto = HouseTestBuilder.builder()
                .withHistoryDate(date.toString())
                .build()
                .buildHouseWithHistoryDto();

        List<Object[]> pastOwners = new ArrayList<>();
        pastOwners.add(new Object[]{house, date});

        Mockito.when(houseRepository.getPastOwnedHousesByPersonUuid(house.getUuid()))
                .thenReturn(pastOwners);
        when(houseMapper.toHouseWithHistoryDto(house, date))
                .thenReturn(houseWithHistoryDto);

        List<HouseWithHistoryDto> result = personService.getOwnedHousesHistoryByPersonUuid(house.getUuid());

        assertNotNull(result);
        assertEquals(1, result.size());

        HouseWithHistoryDto dto = result.get(0);
        assertNotNull(dto); // Проверяем, что dto не null

        Assertions.assertEquals(house.getUuid(), dto.getUuid());
        Assertions.assertEquals(date.toString(), dto.getHistoryDate());
    }

}
