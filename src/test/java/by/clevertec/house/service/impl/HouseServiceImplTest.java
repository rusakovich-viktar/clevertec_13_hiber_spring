package by.clevertec.house.service.impl;

import static by.clevertec.house.util.Constant.Attributes.AREA;
import static by.clevertec.house.util.Constant.Attributes.CITY;
import static by.clevertec.house.util.Constant.Attributes.COUNTRY;
import static by.clevertec.house.util.Constant.Attributes.NUMBER;
import static by.clevertec.house.util.Constant.Attributes.STREET;
import static by.clevertec.house.util.Constant.Attributes.VALUE_ONE_TO_SIMPLIFY_UI;
import static by.clevertec.house.util.TestConstant.HOUSE_TWO_AREA;
import static by.clevertec.house.util.TestConstant.HOUSE_TWO_CITY;
import static by.clevertec.house.util.TestConstant.HOUSE_TWO_COUNTRY;
import static by.clevertec.house.util.TestConstant.HOUSE_TWO_NUMBER;
import static by.clevertec.house.util.TestConstant.HOUSE_TWO_STREET;
import static by.clevertec.house.util.TestConstant.PAGE_NUMBER_SHOULD_BE_POSITIVE;
import static by.clevertec.house.util.TestConstant.PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import by.clevertec.exception.EntityNotFoundException;
import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.dto.PersonWithHistoryDto;
import by.clevertec.house.entity.House;
import by.clevertec.house.entity.Person;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.repository.HouseRepository;
import by.clevertec.house.repository.PersonRepository;
import by.clevertec.house.util.HouseTestBuilder;
import by.clevertec.house.util.PersonTestBuilder;
import by.clevertec.house.util.TestConstant;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class HouseServiceImplTest {

    @Mock
    private HouseRepository houseRepository;
    @Mock
    private PersonRepository personRepository;

    @Mock
    private Validator validator;

    @Mock
    private HouseMapper houseMapper;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private HouseServiceImpl houseService;

    @Captor
    private ArgumentCaptor<House> captor;
    private final UUID randomUuid = UUID.randomUUID();

    @Test
    void testGetHouseByUuidShouldReturnHouse_whenItExist() {

        // given
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        HouseResponseDto expected = HouseTestBuilder.builder()
                .build()
                .buildHouseResponseDto();

        UUID uuid = house.getUuid();

        // when
        when(houseRepository.findByUuid(uuid))
                .thenReturn(Optional.of(house));
        when(houseMapper.toDto(house))
                .thenReturn(expected);

        HouseResponseDto actual = houseService.getHouseByUuid(uuid);

        // then
        verify(houseRepository).findByUuid(uuid);
        verify(houseMapper).toDto(house);
        assertThat(actual)
                .hasFieldOrPropertyWithValue(House.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(House.Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(House.Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(House.Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(House.Fields.street, expected.getStreet())
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber());
    }

    @Test
    void testGetHouseByUuidShouldThrowNotFound_whenInvalidUuid() {
        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                houseService.getHouseByUuid(randomUuid));

        assertEquals(exception.getClass(), EntityNotFoundException.class);

        verify(houseRepository).findByUuid(randomUuid);
        verifyNoMoreInteractions(houseRepository, houseMapper);
    }

    @Test
    void testGetAllHouses() {

        // given
        Page page = mock(Page.class);
        ArgumentCaptor<PageRequest> captor = ArgumentCaptor.forClass(PageRequest.class);
        when(houseRepository.findAll(any(PageRequest.class)))
                .thenReturn(page);

        // when
        houseService.getAllHouses(PAGE_NUMBER_SHOULD_BE_POSITIVE, PAGE_SIZE);

        // then
        verify(houseRepository).findAll(captor.capture());
        PageRequest pageRequest = captor.getValue();
        assertEquals(PAGE_NUMBER_SHOULD_BE_POSITIVE - VALUE_ONE_TO_SIMPLIFY_UI, pageRequest.getPageNumber());
        assertEquals(PAGE_SIZE, pageRequest.getPageSize());

        verifyNoMoreInteractions(houseRepository);
    }

    @Test
    void testSaveHouseShouldSaveCorrectHouse() {
        // given
        House expectedHouse = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        HouseRequestDto houseRequestDto = HouseTestBuilder.builder()
                .build()
                .buildHouseRequestDto();

        when(houseMapper.toEntity(houseRequestDto))
                .thenReturn(expectedHouse);

        // when
        houseService.saveHouse(houseRequestDto);

        // then
        verify(houseRepository).save(captor.capture());

        House savedHouse = captor.getValue();
        assertNotNull(savedHouse.getCreateDate(), "CreateDate should be set");
        assertEquals(expectedHouse, savedHouse,
                "Saved house should match expected house");
    }

    @Test
    void testUpdateHouseShouldUpdateAndSaveCorrectHouse() {

        // given
        HouseRequestDto houseDto = HouseTestBuilder.builder()
                .build()
                .buildHouseRequestDto();

        House existingHouse = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        when(houseRepository.findByUuid(existingHouse.getUuid()))
                .thenReturn(Optional.of(existingHouse));
        when(validator.validate(houseDto))
                .thenReturn(Collections.emptySet());

        // when
        houseService.updateHouse(existingHouse.getUuid(), houseDto);

        // then
        verify(houseRepository).save(captor.capture());
        House savedHouse = captor.getValue();
        assertEquals(houseDto.getArea(), savedHouse.getArea());
        assertEquals(houseDto.getCountry(), savedHouse.getCountry());
        assertEquals(houseDto.getCity(), savedHouse.getCity());
        assertEquals(houseDto.getStreet(), savedHouse.getStreet());
        assertEquals(houseDto.getNumber(), savedHouse.getNumber());

        verify(validator).validate(houseDto);
        verify(houseRepository).findByUuid(existingHouse.getUuid());
        verify(houseMapper).updateHouseFromDto(houseDto, existingHouse);
    }

    @Test
    void testUpdateHouseShouldThrowConstraintViolationException_whenDtoIsInvalid() {
        // given
        HouseRequestDto houseDto = HouseTestBuilder.builder()
                .build()
                .buildHouseRequestDto();

        Set<ConstraintViolation<HouseRequestDto>> violations = new HashSet<>();
        violations.add(mock(ConstraintViolation.class));

        when(validator.validate(houseDto))
                .thenReturn(violations);

        // then
        assertThrows(ConstraintViolationException.class, () -> {
            // when
            houseService.updateHouse(UUID.randomUUID(), houseDto);
        });
    }

    @Test
    void testDeleteHouseShouldCallRepositoryDeleteMethod() {
        // when
        houseService.deleteHouse(randomUuid);

        // then
        verify(houseRepository).deleteByUuid(randomUuid);
    }

    @Test
    void testUpdateHouseFieldsShouldUpdateFieldsCorrectly() {
        // given
        House existingHouse = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        Map<String, Object> updates = new HashMap<>();
        updates.put(AREA, HOUSE_TWO_AREA);
        updates.put(COUNTRY, HOUSE_TWO_COUNTRY);
        updates.put(CITY, HOUSE_TWO_CITY);
        updates.put(STREET, HOUSE_TWO_STREET);
        updates.put(NUMBER, HOUSE_TWO_NUMBER);

        when(houseRepository.findByUuid(existingHouse.getUuid()))
                .thenReturn(Optional.of(existingHouse));

        // when
        houseService.updateHouseFields(existingHouse.getUuid(), updates);

        // then
        verify(houseRepository).findByUuid(existingHouse.getUuid());
        verify(houseRepository).save(captor.capture());
        House savedHouse = captor.getValue();

        assertEquals(HOUSE_TWO_AREA, savedHouse.getArea());
        assertEquals(HOUSE_TWO_COUNTRY, savedHouse.getCountry());
        assertEquals(HOUSE_TWO_CITY, savedHouse.getCity());
        assertEquals(HOUSE_TWO_STREET, savedHouse.getStreet());
        assertEquals(HOUSE_TWO_NUMBER, savedHouse.getNumber());
        verify(houseRepository).save(existingHouse);
    }

    @Test
    void testUpdateHouseFieldsShouldThrowException_whenInvalidUuid() {
        // given
        Map<String, Object> updates = new HashMap<>();
        updates.put(AREA, HOUSE_TWO_AREA);
        updates.put(COUNTRY, HOUSE_TWO_COUNTRY);
        updates.put(CITY, HOUSE_TWO_CITY);
        updates.put(STREET, HOUSE_TWO_STREET);
        updates.put(NUMBER, HOUSE_TWO_NUMBER);

        when(houseRepository.findByUuid(randomUuid))
                .thenReturn(Optional.empty());

        // when / then
        assertThrows(EntityNotFoundException.class, () ->
                houseService.updateHouseFields(randomUuid, updates));

        // then
        verify(houseRepository).findByUuid(randomUuid);
        verifyNoMoreInteractions(houseRepository);
    }

    @Test
    void testUpdateHouseShouldThrowException_whenInvalidUuid() {
        // given
        HouseRequestDto houseDto = HouseTestBuilder.builder()
                .build()
                .buildHouseRequestDto();

        when(houseRepository.findByUuid(randomUuid))
                .thenReturn(Optional.empty());

        // when
        assertThrows(EntityNotFoundException.class, () ->
                houseService.updateHouse(randomUuid, houseDto));

        // then
        verify(houseRepository).findByUuid(randomUuid);
        verifyNoMoreInteractions(houseRepository, houseMapper);
    }

    @Test
    void testGetTenantsByHouseUuidShouldReturnCorrectList_whenItExist() {
        // given
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        Person person = PersonTestBuilder.builder()
                .build()
                .buildPerson();
        house.setTenants(List.of(person));

        PersonResponseDto personResponseDto = PersonTestBuilder.builder()
                .build()
                .buildPersonResponseDto();
        List<PersonResponseDto> expected = List.of(personResponseDto);

        when(houseRepository.findByUuid(house.getUuid()))
                .thenReturn(Optional.of(house));
        when(personMapper.toDto(person))
                .thenReturn(personResponseDto);

        // when
        List<PersonResponseDto> actual = houseService.getPersonsWhoLiveHereNowByHouseUuid(house.getUuid());

        // then
        verify(houseRepository)
                .findByUuid(house.getUuid());
        assertEquals(expected, actual);
        verifyNoMoreInteractions(houseRepository, personMapper);

    }

    @Test
    void testGetTenantsByHouseUuidShouldThrowException_whenHouseDoesNotExist() {
        // given
        when(houseRepository.findByUuid(randomUuid))
                .thenReturn(Optional.empty());

        // when / then
        assertThrows(EntityNotFoundException.class, () ->
                houseService.getPersonsWhoLiveHereNowByHouseUuid(randomUuid));

        verify(houseRepository)
                .findByUuid(randomUuid);
        verifyNoMoreInteractions(houseRepository, personMapper);
    }

    @Test
    void testGetPastTenantsByHouseUuidShouldReturnListPersonWithHistoryDto() {

        Person person = PersonTestBuilder.builder()
                .build()
                .buildPerson();
        LocalDateTime date = TestConstant.PERSON_ONE_CREATE_DATE;

        PersonWithHistoryDto personWithHistoryDto = PersonTestBuilder.builder()
                .withHistoryDate(date.toString())
                .build()
                .buildPersonWithHistoryDto();

        List<Object[]> pastTenants = new ArrayList<>();
        pastTenants.add(new Object[]{person, date});

        when(personRepository.findPastTenantsByHouseUuid(person.getHouse().getUuid()))
                .thenReturn(pastTenants);
        when(personMapper.toPersonWithHistoryDto(person, date))
                .thenReturn(personWithHistoryDto);

        List<PersonWithHistoryDto> result = houseService.getPersonsWithHistoryWhoLivedHereInPastByHouseUuid(person.getHouse().getUuid());

        assertNotNull(result);
        assertEquals(1, result.size());

        PersonWithHistoryDto dto = result.get(0);
        assertNotNull(dto);

        Assertions.assertEquals(person.getName(), dto.getName());
        Assertions.assertEquals(date.toString(), dto.getHistoryDate());
    }

    @Test
    void testGetPastOwnersByHouseUuidShouldReturnListPersonWithHistoryDto() {

        Person person = PersonTestBuilder.builder()
                .build()
                .buildPerson();
        LocalDateTime date = TestConstant.PERSON_ONE_CREATE_DATE;

        PersonWithHistoryDto personWithHistoryDto = PersonTestBuilder.builder()
                .withHistoryDate(date.toString())
                .build()
                .buildPersonWithHistoryDto();


        List<Object[]> pastOwners = new ArrayList<>();
        pastOwners.add(new Object[]{person, date});

        when(personRepository.findPastOwnersByHouseUuid(person.getHouse().getUuid()))
                .thenReturn(pastOwners);
        when(personMapper.toPersonWithHistoryDto(person, date))
                .thenReturn(personWithHistoryDto);

        List<PersonWithHistoryDto> result =
                houseService.getPersonsWithHistoryWhoOwnedThisHouseByHouseUuid(person.getHouse().getUuid());

        assertNotNull(result);
        assertEquals(1, result.size());

        PersonWithHistoryDto dto = result.get(0);
        assertNotNull(dto);

        Assertions.assertEquals(person.getName(), dto.getName());
        Assertions.assertEquals(date.toString(), dto.getHistoryDate());
    }
}
