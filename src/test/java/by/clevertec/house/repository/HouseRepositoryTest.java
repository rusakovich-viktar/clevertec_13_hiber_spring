package by.clevertec.house.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import by.clevertec.house.entity.House;
import by.clevertec.house.entity.Person;
import by.clevertec.house.util.HouseTestBuilder;
import by.clevertec.house.util.PersonTestBuilder;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = "classpath:db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class HouseRepositoryTest {

    private final HouseRepository houseRepository;

    @Test
    void findByUuidShouldReturnHouse_whenItExist() {
        // given
        House expected = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        // when
        Optional<House> actual = houseRepository.findByUuid(expected.getUuid());

        // then
        assertTrue(actual.isPresent());
        assertThat(actual.get())
                .hasFieldOrPropertyWithValue(House.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(House.Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(House.Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(House.Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(House.Fields.street, expected.getStreet())
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber())
                .hasFieldOrPropertyWithValue(House.Fields.createDate, expected.getCreateDate());
    }

    @Test
    @Sql(value = "classpath:db/dml/insert-only-one-house.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteByUuidShouldRemoveHouse_whenUuidExists() {
        // given
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        // when
        houseRepository.deleteByUuid(house.getUuid());
        Optional<House> actual = houseRepository.findByUuid(house.getUuid());

        // then
        assertFalse(actual.isPresent());
    }

    @Test
    void getOwnedHousesByPersonUuidShouldReturnList_whenItExist() {
        // given
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();
        Person person = PersonTestBuilder.builder()
                .withHouse(house)
                .build()
                .buildPerson();

        List<House> expected = List.of(house);

        List<House> actual = houseRepository.getOwnedHousesByPersonUuid(person.getUuid());

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getUuid(), actual.get(0).getUuid());

    }

    @Test
    void getPastTenantsByUuid() {
    }

    @Test
    void findPastOwnedHousesByUuid() {
    }
}
