package by.clevertec.house.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.dto.PersonWithHistoryDto;
import by.clevertec.house.entity.House;
import by.clevertec.house.entity.Sex;
import by.clevertec.house.exception.EntityNotFoundException;
import by.clevertec.house.service.HouseService;
import by.clevertec.house.util.HouseTestBuilder;
import by.clevertec.house.util.PersonTestBuilder;
import by.clevertec.house.util.TestConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RequiredArgsConstructor
@WebMvcTest(HouseController.class)
class HouseControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UUID randomUUID = UUID.randomUUID();

    @MockBean
    private final HouseService houseService;

    @Nested
    class TestGetByUuid {

        @Test
        void getByUuidShouldReturnHouseResponseDto() throws Exception {
            HouseResponseDto expected = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();

            when(houseService.getHouseByUuid(expected.getUuid()))
                    .thenReturn(expected);

            mockMvc.perform(get("/houses/" + expected.getUuid()))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expected)))
                    .andExpect(jsonPath("$.uuid", is(expected.getUuid().toString())))
                    .andExpect(jsonPath("$.area", is(expected.getArea())))
                    .andExpect(jsonPath("$.country", is(expected.getCountry())))
                    .andExpect(jsonPath("$.city", is(expected.getCity())))
                    .andExpect(jsonPath("$.street", is(expected.getStreet())))
                    .andExpect(jsonPath("$.number", is(expected.getNumber())))
                    .andExpect(jsonPath("$.createDate", is(expected.getCreateDate())));

        }

        @Test
        void getByUuidShouldShouldThrowNotFound_whenInvalidUuid() throws Exception {

            String url = "/houses/" + randomUUID;
            EntityNotFoundException exception = EntityNotFoundException.of(House.class, randomUUID);

            when(houseService.getHouseByUuid(randomUUID)).thenThrow(exception);

            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message", is(exception.getMessage())));
        }
    }

    @Nested
    class TestGetAll {

        @Test
        void getAllHousesShouldReturnListOfHouses() throws Exception {
            // given
            int pageNumber = 1;
            int pageSize = 15;
            HouseResponseDto houseOne = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();

            HouseResponseDto houseTwo = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();

            List<HouseResponseDto> expectedHouses = List.of(houseOne, houseTwo);
            when(houseService.getAllHouses(pageNumber, pageSize))
                    .thenReturn(expectedHouses);

            // when & then
            mockMvc.perform(get("/houses")
                            .param("pageNumber", String.valueOf(pageNumber))
                            .param("pageSize", String.valueOf(pageSize)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expectedHouses)))

                    .andExpect(jsonPath("$", hasSize(expectedHouses.size())))
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
    }

    @Nested
    class TestSaveHouse {

        @Test
        void saveHouseShouldReturnCreated() throws Exception {
            // given
            HouseRequestDto requestDto = HouseTestBuilder.builder()
                    .build()
                    .buildHouseRequestDto();

            // when & then
            mockMvc.perform(post("/houses")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(requestDto)))
                    .andExpect(status().isCreated());

            verify(houseService, times(1))
                    .saveHouse(requestDto);
        }

        @Test
        void saveHouseShouldReturnBadRequest_whenMissingRequiredFields() throws Exception {
            // given
            HouseRequestDto requestDto = new HouseRequestDto();

            // when & then
            mockMvc.perform(post("/houses")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class TestUpdateHouse {

        @Test
        void updateHouseShouldReturnOk() throws Exception {
            // given
            HouseRequestDto requestDto = HouseTestBuilder.builder()
                    .build()
                    .buildHouseRequestDto();

            // when & then
            mockMvc.perform(put("/houses/" + requestDto.getUuid())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(requestDto)))
                    .andExpect(status().isOk());

            verify(houseService, times(1))
                    .updateHouse(requestDto.getUuid(), requestDto);
        }

        @Test
        void updateHouseShouldReturnBadRequest_whenInvalidData() throws Exception {
            // given
            HouseRequestDto requestDto = new HouseRequestDto();

            // when & then
            mockMvc.perform(put("/houses/" + randomUUID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class TestUpdateHouseFields {

        @Test
        void updateHouseFieldsShouldReturnOk() throws Exception {
            // given
            UUID uuid = TestConstant.HOUSE_ONE_UUID;
            Map<String, Object> updates = new HashMap<>();
            updates.put("area", 300.0);
            updates.put("city", "New City");

            // when & then
            mockMvc.perform(patch("/houses/" + uuid)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(updates)))
                    .andExpect(status().isOk());

            verify(houseService, times(1))
                    .updateHouseFields(uuid, updates);
        }
    }

    @Nested
    class TestDeleteHouse {

        @Test
        void deleteHouseShouldReturnNoContent() throws Exception {
            // given
            UUID uuid = TestConstant.HOUSE_ONE_UUID;

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.delete("/houses/" + uuid))
                    .andExpect(status().isNoContent());

            verify(houseService, times(1))
                    .deleteHouse(uuid);
        }
    }

    @Nested
    class TestGetTenants {

        @Test
        void getTenantsShouldReturnListOfPersonResponseDto() throws Exception {
            // given
            UUID uuid = TestConstant.HOUSE_ONE_UUID;
            PersonResponseDto tenantOne = PersonTestBuilder.builder()
                    .build()
                    .buildPersonResponseDto();
            PersonResponseDto tenantTwo = PersonTestBuilder.builder()
                    .withUuid(randomUUID)
                    .withName("Anna")
                    .withSurname("Asti")
                    .withSex(Sex.FEMALE)
                    .build()
                    .buildPersonResponseDto();

            List<PersonResponseDto> expectedTenants = List.of(tenantOne, tenantTwo);
            when(houseService.getTenantsByHouseUuid(uuid))
                    .thenReturn(expectedTenants);

            // when & then
            mockMvc.perform(get("/houses/" + uuid + "/tenants"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].uuid", is(tenantOne.getUuid().toString())))
                    .andExpect(jsonPath("$[0].name", is(tenantOne.getName())))
                    .andExpect(jsonPath("$[0].surname", is(tenantOne.getSurname())))
                    .andExpect(jsonPath("$[0].sex", is(tenantOne.getSex().toString())))
                    .andExpect(jsonPath("$[0].createDate", is(tenantOne.getCreateDate())))
                    .andExpect(jsonPath("$[0].updateDate", is(tenantOne.getUpdateDate())))
                    .andExpect(jsonPath("$[1].uuid", is(tenantTwo.getUuid().toString())))
                    .andExpect(jsonPath("$[1].name", is(tenantTwo.getName())))
                    .andExpect(jsonPath("$[1].surname", is(tenantTwo.getSurname())))
                    .andExpect(jsonPath("$[1].sex", is(tenantTwo.getSex().toString())))
                    .andExpect(jsonPath("$[1].createDate", is(tenantTwo.getCreateDate())))
                    .andExpect(jsonPath("$[1].updateDate", is(tenantTwo.getUpdateDate())));
        }

        @Test
        void getTenantsShouldReturnEmptyList_whenNoPersons() throws Exception {
            // given
            when(houseService.getTenantsByHouseUuid(randomUUID))
                    .thenReturn(List.of());

            // when & then
            mockMvc.perform(get("/houses/" + randomUUID + "/tenants"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", Matchers.hasSize(0)));
        }
    }

    @Nested
    class TestGetPastTenants {

        @Test
        void getTenantsHistoryShouldReturnListOfPersons() throws Exception {
            // given
            PersonWithHistoryDto personOne = PersonTestBuilder.builder()
                    .build()
                    .buildPersonWithHistoryDto();

            PersonWithHistoryDto personTwo = PersonTestBuilder.builder()
                    .withUuid(randomUUID)
                    .build()
                    .buildPersonWithHistoryDto();

            List<PersonWithHistoryDto> expectedPersons = List.of(personOne, personTwo);
            when(houseService.getPastTenantsByHouseUuid(randomUUID))
                    .thenReturn(expectedPersons);

            // when & then
            mockMvc.perform(get("/houses/" + randomUUID + "/tenants/history"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].uuid", is(personOne.getUuid().toString())))
                    .andExpect(jsonPath("$[0].name", is(personOne.getName())))
                    .andExpect(jsonPath("$[0].surname", is(personOne.getSurname())))
                    .andExpect(jsonPath("$[0].historyDate", is(personOne.getHistoryDate())))
                    .andExpect(jsonPath("$[1].uuid", is(personTwo.getUuid().toString())))
                    .andExpect(jsonPath("$[1].name", is(personTwo.getName())))
                    .andExpect(jsonPath("$[1].surname", is(personTwo.getSurname())))
                    .andExpect(jsonPath("$[1].historyDate", is(personTwo.getHistoryDate())));
        }

        @Test
        void getTenantsHistoryShouldReturnEmptyList_whenNoPersons() throws Exception {
            // given
            when(houseService.getPastTenantsByHouseUuid(randomUUID))
                    .thenReturn(List.of());

            // when & then
            mockMvc.perform(get("/houses/" + randomUUID + "/tenants/history"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", Matchers.hasSize(0)));
        }
    }

    @Nested
    class TestGetPastOwners {

        @Test
        void getOwnersHistoryShouldReturnListOfPersons() throws Exception {
            // given
            PersonWithHistoryDto personOne = PersonTestBuilder.builder()
                    .build()
                    .buildPersonWithHistoryDto();

            PersonWithHistoryDto personTwo = PersonTestBuilder.builder()
                    .withUuid(randomUUID)
                    .build()
                    .buildPersonWithHistoryDto();

            List<PersonWithHistoryDto> expectedPersons = List.of(personOne, personTwo);
            when(houseService.getPastOwnersByHouseUuid(randomUUID))
                    .thenReturn(expectedPersons);

            // when & then
            mockMvc.perform(get("/houses/" + randomUUID + "/owners/history"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].uuid", is(personOne.getUuid().toString())))
                    .andExpect(jsonPath("$[0].name", is(personOne.getName())))
                    .andExpect(jsonPath("$[0].surname", is(personOne.getSurname())))
                    .andExpect(jsonPath("$[0].historyDate", is(personOne.getHistoryDate())))
                    .andExpect(jsonPath("$[1].uuid", is(personTwo.getUuid().toString())))
                    .andExpect(jsonPath("$[1].name", is(personTwo.getName())))
                    .andExpect(jsonPath("$[1].surname", is(personTwo.getSurname())))
                    .andExpect(jsonPath("$[1].historyDate", is(personTwo.getHistoryDate())));
        }

        @Test
        void getOwnersHistoryShouldReturnEmptyList_whenNoPersons() throws Exception {
            // given
            when(houseService.getPastOwnersByHouseUuid(randomUUID))
                    .thenReturn(List.of());

            // when & then
            mockMvc.perform(get("/houses/" + randomUUID + "/owners/history"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", Matchers.hasSize(0)));
        }
    }
}
