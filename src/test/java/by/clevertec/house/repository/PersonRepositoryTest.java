package by.clevertec.house.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import by.clevertec.house.entity.Person;
import by.clevertec.house.entity.Person.Fields;
import by.clevertec.house.util.PersonTestBuilder;
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
class PersonRepositoryTest {

    private final PersonRepository personRepository;

    @Test
    void findByUuidShouldReturnPerson_whenItExist() {
        // given
        Person expected = PersonTestBuilder.builder()
                .build()
                .buildPerson();

        // when
        Optional<Person> actual = personRepository.findByUuid(expected.getUuid());

        // then
        assertTrue(actual.isPresent());
        assertThat(actual.get())
                .hasFieldOrPropertyWithValue(Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(Fields.sex, expected.getSex())
                .hasFieldOrPropertyWithValue(Fields.updateDate, expected.getUpdateDate())
                .hasFieldOrPropertyWithValue(Person.Fields.createDate, expected.getCreateDate());

    }

    @Test
    @Sql(value = "classpath:db/dml/insert-only-person-not-owner.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteByUuidShouldRemovePerson_whenUuidExists() {
        // given
        Person expected = PersonTestBuilder.builder()
                .build()
                .buildPerson();

        // when
        personRepository.deleteByUuid(expected.getUuid());
        Optional<Person> actual = personRepository.findByUuid(expected.getUuid());

        // then
        assertFalse(actual.isPresent());
    }

    @Test
    void findPastTenantsByHouseUuid() {
    }

    @Test
    void findPastOwnersByHouseUuid() {
    }
}