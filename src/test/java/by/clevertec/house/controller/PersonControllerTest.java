package by.clevertec.house.controller;

import static by.clevertec.house.util.TestConstant.ALEX;
import static by.clevertec.house.util.TestConstant.ALEXEEY;
import static by.clevertec.house.util.TestConstant.HOUSE_TWO_UUID;
import static by.clevertec.house.util.TestConstant.PAGE_NUMBER_SHOULD_BE_POSITIVE;
import static by.clevertec.house.util.TestConstant.PAGE_SIZE;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.HouseWithHistoryDto;
import by.clevertec.house.dto.PersonRequestDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.entity.Person;
import by.clevertec.house.exception.EntityNotFoundException;
import by.clevertec.house.service.PersonService;
import by.clevertec.house.util.Constant.Attributes;
import by.clevertec.house.util.HouseTestBuilder;
import by.clevertec.house.util.PersonTestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@RequiredArgsConstructor
@WebMvcTest(PersonController.class)
class PersonControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UUID randomUUID = UUID.randomUUID();

    @MockBean
    private final PersonService personService;


    @Nested
    class TestGetPersonByUuid {

        @Test
        void getPersonByUuidShouldReturnPersonResponseDto() throws Exception {
            // given

            PersonResponseDto expected = PersonTestBuilder.builder()
                    .build()
                    .buildPersonResponseDto();

            when(personService.getPersonByUuid(randomUUID))
                    .thenReturn(expected);

            // when & then
            mockMvc.perform(get("/persons/" + randomUUID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.uuid", is(expected.getUuid().toString())))
                    .andExpect(jsonPath("$.name", is(expected.getName())))
                    .andExpect(jsonPath("$.surname", is(expected.getSurname())))
                    .andExpect(jsonPath("$.sex", is(expected.getSex().toString())))
                    .andExpect(jsonPath("$.createDate", is(expected.getCreateDate())))
                    .andExpect(jsonPath("$.updateDate", is(expected.getUpdateDate())));
        }

        @Test
        void getByUuidShouldShouldThrowNotFound_whenInvalidUuid() throws Exception {

            String url = "/persons/" + randomUUID;
            EntityNotFoundException exception = EntityNotFoundException.of(Person.class, randomUUID);

            when(personService.getPersonByUuid(randomUUID))
                    .thenThrow(exception);

            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message", is(exception.getMessage())));
        }
    }

    @Nested
    class TestGetAllPersons {

        @Test
        void getAllPersonsShouldReturnListOfPersons() throws Exception {
            // given
            int pageNumber = PAGE_NUMBER_SHOULD_BE_POSITIVE;
            int pageSize = PAGE_SIZE;
            PersonResponseDto personOne = PersonTestBuilder.builder()
                    .build()
                    .buildPersonResponseDto();

            PersonResponseDto personTwo = PersonTestBuilder.builder()
                    .build()
                    .buildPersonResponseDto();

            List<PersonResponseDto> expectedPersons = List.of(personOne, personTwo);
            when(personService.getAllPersons(pageNumber, pageSize))
                    .thenReturn(expectedPersons);

            // when & then
            mockMvc.perform(get("/persons")
                            .param("pageNumber", String.valueOf(pageNumber))
                            .param("pageSize", String.valueOf(pageSize)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(expectedPersons.size())))
                    .andExpect(jsonPath("$[0].uuid", is(personOne.getUuid().toString())))
                    .andExpect(jsonPath("$[0].name", is(personOne.getName())))
                    .andExpect(jsonPath("$[0].surname", is(personOne.getSurname())))
                    .andExpect(jsonPath("$[0].sex", is(personOne.getSex().toString())))
                    .andExpect(jsonPath("$[0].createDate", is(personOne.getCreateDate())))
                    .andExpect(jsonPath("$[0].updateDate", is(personOne.getUpdateDate())))
                    .andExpect(jsonPath("$[1].uuid", is(personTwo.getUuid().toString())))
                    .andExpect(jsonPath("$[1].name", is(personTwo.getName())))
                    .andExpect(jsonPath("$[1].surname", is(personTwo.getSurname())))
                    .andExpect(jsonPath("$[1].sex", is(personTwo.getSex().toString())))
                    .andExpect(jsonPath("$[1].createDate", is(personTwo.getCreateDate())))
                    .andExpect(jsonPath("$[1].updateDate", is(personTwo.getUpdateDate())));
        }
    }

    @Nested
    class TestSavePerson {

        @Test
        void savePersonShouldReturnCreated() throws Exception {
            // given
            PersonRequestDto requestDto = PersonTestBuilder.builder()
                    .build()
                    .buildPersonRequestDto();

            // when & then
            mockMvc.perform(post("/persons")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isCreated());

            verify(personService, times(1))
                    .savePerson(requestDto);
        }

        @Test
        void savePersonShouldReturnBadRequest_whenMissingRequiredFields() throws Exception {
            // given
            PersonRequestDto requestDto = new PersonRequestDto();

            // when & then
            mockMvc.perform(post("/persons")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class TestUpdatePerson {

        @Test
        void updatePersonShouldReturnOk() throws Exception {
            // given
            PersonRequestDto requestDto = PersonTestBuilder.builder()
                    .build()
                    .buildPersonRequestDto();

            // when & then
            mockMvc.perform(put("/persons/" + requestDto.getUuid())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk());

            verify(personService, times(1))
                    .updatePerson(requestDto.getUuid(), requestDto);
        }

        @Test
        void updatePersonShouldReturnNotFound_whenPersonDoesNotExist() throws Exception {
            // given
            PersonRequestDto requestDto = new PersonRequestDto();

            // when & then
            mockMvc.perform(put("/persons/" + randomUUID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class TestUpdatePersonFields {

        @Test
        void updatePersonFieldsShouldReturnOk() throws Exception {
            // given
            Map<String, Object> updates = new HashMap<>();
            updates.put(Attributes.NAME, ALEX);
            updates.put(Attributes.SURNAME, ALEXEEY);

            // when & then
            mockMvc.perform(patch("/persons/" + randomUUID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updates)))
                    .andExpect(status().isOk());

            verify(personService, times(1))
                    .updatePersonFields(randomUUID, updates);
        }

        @Test
        void updatePersonShouldReturnBadRequest_whenInvalidData() throws Exception {
            // given
            PersonRequestDto requestDto = new PersonRequestDto();

            // when & then
            mockMvc.perform(put("/persons/" + randomUUID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class TestDeletePerson {

        @Test
        void deletePersonShouldReturnNoContent() throws Exception {
            // given

            // when & then
            mockMvc.perform(delete("/persons/" + randomUUID))
                    .andExpect(status().isNoContent());

            verify(personService, times(1))
                    .deletePerson(randomUUID);
        }
    }

    @Nested
    class TestGetOwnedHouses {

        @Test
        void getOwnedHousesShouldReturnListOfHouses() throws Exception {
            // given

            HouseResponseDto houseOne = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();

            HouseResponseDto houseTwo = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();

            List<HouseResponseDto> expectedHouses = List.of(houseOne, houseTwo);

            when(personService.getOwnedHousesByPersonUuid(randomUUID))
                    .thenReturn(expectedHouses);

            // when & then
            mockMvc.perform(get("/persons/" + randomUUID + "/owned-houses"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].uuid", is(houseOne.getUuid().toString())))
                    .andExpect(jsonPath("$[0].area", is(houseOne.getArea())))
                    .andExpect(jsonPath("$[0].country", is(houseOne.getCountry())))
                    .andExpect(jsonPath("$[0].city", is(houseOne.getCity())))
                    .andExpect(jsonPath("$[0].street", is(houseOne.getStreet())))
                    .andExpect(jsonPath("$[0].number", is(houseOne.getNumber())))
                    .andExpect(jsonPath("$[0].createDate", is(houseOne.getCreateDate())))
                    .andExpect(jsonPath("$[1].uuid", is(houseTwo.getUuid().toString())))
                    .andExpect(jsonPath("$[1].area", is(houseTwo.getArea())))
                    .andExpect(jsonPath("$[1].country", is(houseTwo.getCountry())))
                    .andExpect(jsonPath("$[1].city", is(houseTwo.getCity())))
                    .andExpect(jsonPath("$[1].street", is(houseTwo.getStreet())))
                    .andExpect(jsonPath("$[1].number", is(houseTwo.getNumber())))
                    .andExpect(jsonPath("$[1].createDate", is(houseTwo.getCreateDate())));
        }

        @Test
        void getOwnedHousesShouldReturnEmptyList_whenNoHouses() throws Exception {
            // given
            when(personService.getOwnedHousesByPersonUuid(randomUUID))
                    .thenReturn(List.of());

            // when & then
            mockMvc.perform(get("/persons/" + randomUUID + "/owned-houses"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    class TestGetPastResidences {

        @Test
        void getPastResidencesShouldReturnListOfHouses() throws Exception {
            // given
            HouseWithHistoryDto houseOne = HouseTestBuilder.builder()
                    .build()
                    .buildHouseWithHistoryDto();

            HouseWithHistoryDto houseTwo = HouseTestBuilder.builder()
                    .withUuid(HOUSE_TWO_UUID)
                    .build()
                    .buildHouseWithHistoryDto();

            List<HouseWithHistoryDto> expectedHouses = List.of(houseOne, houseTwo);
            when(personService.getTenantedHousesHistoryByPersonUuid(randomUUID))
                    .thenReturn(expectedHouses);

            // when & then
            mockMvc.perform(get("/persons/" + randomUUID + "/tenanted-houses/history"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].uuid", is(houseOne.getUuid().toString())))
                    .andExpect(jsonPath("$[0].area", is(houseOne.getArea())))
                    .andExpect(jsonPath("$[0].country", is(houseOne.getCountry())))
                    .andExpect(jsonPath("$[0].city", is(houseOne.getCity())))
                    .andExpect(jsonPath("$[0].street", is(houseOne.getStreet())))
                    .andExpect(jsonPath("$[0].number", is(houseOne.getNumber())))
                    .andExpect(jsonPath("$[0].historyDate", is(houseOne.getHistoryDate())))
                    .andExpect(jsonPath("$[1].uuid", is(houseTwo.getUuid().toString())))
                    .andExpect(jsonPath("$[1].area", is(houseTwo.getArea())))
                    .andExpect(jsonPath("$[1].country", is(houseTwo.getCountry())))
                    .andExpect(jsonPath("$[1].city", is(houseTwo.getCity())))
                    .andExpect(jsonPath("$[1].street", is(houseTwo.getStreet())))
                    .andExpect(jsonPath("$[1].number", is(houseTwo.getNumber())))
                    .andExpect(jsonPath("$[1].historyDate", is(houseTwo.getHistoryDate())));
        }

        @Test
        void getPastResidencesShouldReturnEmptyList_whenNoHouses() throws Exception {
            // given
            when(personService.getTenantedHousesHistoryByPersonUuid(randomUUID))
                    .thenReturn(List.of());

            // when & then
            mockMvc.perform(get("/persons/" + randomUUID + "/tenanted-houses/history"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    class TestGetOwnedHousesHistory {

        @Test
        void getOwnedHousesHistoryShouldReturnListOfHouses_whenExist() throws Exception {
            // given
            HouseWithHistoryDto houseOne = HouseTestBuilder.builder()
                    .build()
                    .buildHouseWithHistoryDto();

            HouseWithHistoryDto houseTwo = HouseTestBuilder.builder()
                    .withUuid(HOUSE_TWO_UUID)
                    .build()
                    .buildHouseWithHistoryDto();

            List<HouseWithHistoryDto> expectedHouses = List.of(houseOne, houseTwo);
            when(personService.getOwnedHousesHistoryByPersonUuid(randomUUID))
                    .thenReturn(expectedHouses);

            // when & then
            mockMvc.perform(get("/persons/" + randomUUID + "/owned-houses/history"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].uuid", is(houseOne.getUuid().toString())))
                    .andExpect(jsonPath("$[0].area", is(houseOne.getArea())))
                    .andExpect(jsonPath("$[0].country", is(houseOne.getCountry())))
                    .andExpect(jsonPath("$[0].city", is(houseOne.getCity())))
                    .andExpect(jsonPath("$[0].street", is(houseOne.getStreet())))
                    .andExpect(jsonPath("$[0].number", is(houseOne.getNumber())))
                    .andExpect(jsonPath("$[0].historyDate", is(houseOne.getHistoryDate())))
                    .andExpect(jsonPath("$[1].uuid", is(houseTwo.getUuid().toString())))
                    .andExpect(jsonPath("$[1].area", is(houseTwo.getArea())))
                    .andExpect(jsonPath("$[1].country", is(houseTwo.getCountry())))
                    .andExpect(jsonPath("$[1].city", is(houseTwo.getCity())))
                    .andExpect(jsonPath("$[1].street", is(houseTwo.getStreet())))
                    .andExpect(jsonPath("$[1].number", is(houseTwo.getNumber())))
                    .andExpect(jsonPath("$[1].historyDate", is(houseTwo.getHistoryDate())));
        }

        @Test
        void getOwnedHousesHistoryShouldReturnEmptyList_whenNoHouses() throws Exception {
            // given
            when(personService.getOwnedHousesHistoryByPersonUuid(randomUUID))
                    .thenReturn(List.of());

            // when & then
            mockMvc.perform(get("/persons/" + randomUUID + "/owned-houses/history"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }
}
