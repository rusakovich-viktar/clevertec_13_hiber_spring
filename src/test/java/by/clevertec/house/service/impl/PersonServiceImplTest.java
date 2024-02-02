package by.clevertec.house.service.impl;

import static by.clevertec.house.util.Constant.Attributes.VALUE_ONE_TO_SIMPLIFY_UI;
import static by.clevertec.house.util.TestConstant.ALEX;
import static by.clevertec.house.util.TestConstant.ALEXEEY;
import static by.clevertec.house.util.TestConstant.PAGE_NUMBER_SHOULD_BE_POSITIVE;
import static by.clevertec.house.util.TestConstant.PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.PersonRequestDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.entity.House;
import by.clevertec.house.entity.Person;
import by.clevertec.house.exception.EntityNotFoundException;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.repository.HouseRepository;
import by.clevertec.house.repository.PersonRepository;
import by.clevertec.house.util.Constant.Attributes;
import by.clevertec.house.util.HouseTestBuilder;
import by.clevertec.house.util.PersonTestBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    void getPersonByUuidShouldReturnPerson_whenItExist() {
        // given
        Person person = PersonTestBuilder.builder()
                .build()
                .buildPerson(); // Заполните поля объекта Person
        PersonResponseDto expected = PersonTestBuilder.builder()
                .build()
                .buildPersonResponseDto(); // Заполните поля объекта PersonResponseDto

        UUID uuid = person.getUuid();

        // when
        when(personRepository.findByUuid(uuid))
                .thenReturn(Optional.of(person));
        when(personMapper.toDto(person))
                .thenReturn(expected);

        PersonResponseDto actual = personService.getPersonByUuid(uuid);

        // then
        verify(personMapper).toDto(person);
        assertEquals(expected, actual);
    }


    @Test
    void getAllPersonsShouldReturnAllPersons() {
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

        // then
        List<PersonResponseDto> actualPersons = personService.getAllPersons(PAGE_NUMBER_SHOULD_BE_POSITIVE, PAGE_SIZE);
        assertThat(actualPersons).containsOnly(expectedPersonDto);
    }

    @Test
    void savePersonShouldSaveCorrectPerson() {
        // given
        House house = HouseTestBuilder.builder().build().buildHouse();
        PersonRequestDto personDto = PersonTestBuilder.builder().build().buildPersonRequestDto();
        Person expectedPerson = PersonTestBuilder.builder().build().buildPerson();

        when(houseRepository.findByUuid(house.getUuid())).thenReturn(Optional.of(house));
        when(personMapper.toEntity(personDto)).thenReturn(expectedPerson);
        when(personRepository.save(argThat
                (person -> person.getName().equals(expectedPerson.getName())
                        && person.getSurname().equals(expectedPerson.getSurname()))))
                .thenReturn(expectedPerson);

        // when
        personService.savePerson(personDto);

        // then
        verify(personRepository, times(2)).save(personCaptor.capture());
        Person savedPerson = personCaptor.getValue();
        assertEquals(expectedPerson, savedPerson);
    }

    @Test
    void updatePersonShouldUpdateCorrectPerson() {
        // given
        House house = HouseTestBuilder.builder().build().buildHouse();

        PersonRequestDto personDto = PersonTestBuilder.builder().build().buildPersonRequestDto();
        Person existingPerson = PersonTestBuilder.builder().build().buildPerson();

        when(houseRepository.findByUuid(house.getUuid())).thenReturn(Optional.of(house));
        when(personRepository.findByUuid(existingPerson.getUuid())).thenReturn(Optional.of(existingPerson));

        // when
        personService.updatePerson(existingPerson.getUuid(), personDto);

        // then
        verify(personMapper).updatePersonDetailsFromDto(existingPerson, personDto);
        verify(personRepository).save(existingPerson);
    }


    @Test
    void updatePersonShouldThrowException_whenInvalidUuid() {
        // given
        PersonRequestDto personDto = PersonTestBuilder.builder().build().buildPersonRequestDto();

        when(personRepository.findByUuid(randomUuid)).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> personService.updatePerson(randomUuid, personDto));
    }

    @Test
    void deletePersonShouldTryDeleteEnteredPerson() {
        // given
        Person person = PersonTestBuilder.builder().build().buildPerson();

        when(personRepository.findByUuid(person.getUuid())).thenReturn(Optional.of(person));

        // when
        personService.deletePerson(person.getUuid());

        // then
        verify(personRepository).deleteByUuid(person.getUuid());
    }


    @Test
    void updatePersonFieldsShouldUpdateCorrectPerson() {
        // given
        Map<String, Object> updates = new HashMap<>();
        updates.put(Attributes.NAME, ALEX);
        updates.put(Attributes.SURNAME, ALEXEEY);

        Person existingPerson = PersonTestBuilder.builder().build().buildPerson();

        when(personRepository.findByUuid(existingPerson.getUuid())).thenReturn(Optional.of(existingPerson));

        // when
        personService.updatePersonFields(existingPerson.getUuid(), updates);

        // then
        verify(personRepository).findByUuid(existingPerson.getUuid());
        verify(personRepository).save(personCaptor.capture());
        Person savedPerson = personCaptor.getValue();

        assertEquals(ALEX, savedPerson.getName());
        assertEquals(ALEXEEY, savedPerson.getSurname());
        verify(personRepository).save(existingPerson);
    }

    @Test
    void getOwnedHousesByPersonUuidShouldReturnCorrectList_whenItExist() {
        // given
        House house = HouseTestBuilder.builder().build().buildHouse();
        Person person = PersonTestBuilder.builder().build().buildPerson();
        person.setOwnedHouses(List.of(house));

        when(houseRepository.getOwnedHousesByPersonUuid(person.getUuid())).thenReturn(List.of(house));
        when(houseMapper.toDto(house)).thenReturn(new HouseResponseDto());

        // when
        List<HouseResponseDto> actual = personService.getOwnedHousesByPersonUuid(person.getUuid());

        // then
        assertEquals(1, actual.size());
        verify(houseRepository).getOwnedHousesByPersonUuid(person.getUuid());
    }
}
