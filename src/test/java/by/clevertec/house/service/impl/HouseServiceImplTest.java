package by.clevertec.house.service.impl;

import static by.clevertec.house.util.TestConstant.PAGE_NUMBER_IS_POSITIVE;
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

import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.entity.House;
import by.clevertec.house.exception.EntityNotFoundException;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.repository.HouseRepository;
import by.clevertec.house.repository.PersonRepository;
import by.clevertec.house.util.HouseTestBuilder;
import jakarta.validation.Validator;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
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
    private Validator validator;
    @Mock
    private HouseMapper houseMapper;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private HouseServiceImpl houseService;

    @Captor
    private ArgumentCaptor<House> captor;

    @Test
    void getHouseByUuidShouldReturnHouse_whenItExist() {

        // given
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        HouseResponseDto expected = HouseTestBuilder.builder()
                .build()
                .buildHouseResponseDto();

        UUID uuid = house.getUuid();

        // when
        when(houseRepository.findByUuid(uuid)).thenReturn(Optional.of(house));
        when(houseMapper.toDto(house)).thenReturn(expected);

        HouseResponseDto actual = houseService.getHouseByUuid(uuid);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(House.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(House.Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(House.Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(House.Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(House.Fields.street, expected.getStreet())
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber());
    }

    @Test
    void getHouseByUuidShouldThrowNotFound_whenInvalidUuid() {
        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                houseService.getHouseByUuid(UUID.randomUUID()));

        assertEquals(exception.getClass(), EntityNotFoundException.class);

    }


    @Test
    void getAllHouses() {

        // given
        Page page = mock(Page.class);
        when(houseRepository.findAll(any(PageRequest.class))).thenReturn(page);

        // when
        houseService.getAllHouses(PAGE_NUMBER_IS_POSITIVE, PAGE_SIZE);

        // then
        verify(houseRepository).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(houseRepository);
    }

    @Test
    void saveHouseShouldSaveCorrectHouse() {
        // given
        House expectedHouse = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        HouseRequestDto houseRequestDto = HouseTestBuilder.builder()
                .build()
                .buildHouseRequestDto();

        when(houseMapper.toEntity(houseRequestDto)).thenReturn(expectedHouse);

        // when
        houseService.saveHouse(houseRequestDto);

        // then
        verify(houseRepository).save(captor.capture());

        House savedHouse = captor.getValue();
        assertNotNull(savedHouse.getCreateDate(), "CreateDate should be set");
        assertEquals(expectedHouse, savedHouse, "Saved house should match expected house");
    }


    @Test
    void updateHouseShouldUpdateAndSaveCorrectHouse() {

        // given
        HouseRequestDto houseDto = HouseTestBuilder.builder()
                .build()
                .buildHouseRequestDto();

        House existingHouse = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        when(houseRepository.findByUuid(existingHouse.getUuid())).thenReturn(Optional.of(existingHouse));
        when(validator.validate(houseDto)).thenReturn(Collections.emptySet());

        // when
        houseService.updateHouse(existingHouse.getUuid(), houseDto);

        // then
        verify(validator).validate(houseDto);
        verify(houseRepository).findByUuid(existingHouse.getUuid());
        verify(houseMapper).updateHouseFromDto(houseDto, existingHouse);
        verify(houseRepository).save(existingHouse);


    }

    @Test
    void deleteHouse() {
    }

    @Test
    void updateHouseFields() {
    }

    @Test
    void getTenantsByHouseUuid() {
    }

    @Test
    void getPastTenantsByHouseUuid() {
    }

    @Test
    void getPastOwnersByHouseUuid() {
    }
}