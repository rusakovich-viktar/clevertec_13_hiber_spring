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
import by.clevertec.house.entity.House;
import by.clevertec.house.entity.Sex;
import by.clevertec.house.exception.EntityNotFoundException;
import by.clevertec.house.service.HouseService;
import by.clevertec.house.util.HouseTestBuilder;
import by.clevertec.house.util.HouseTestBuilderTwo;
import by.clevertec.house.util.PersonTestBuilder;
import by.clevertec.house.util.TestConstant;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(HouseController.class)
@RequiredArgsConstructor
class HouseControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @MockBean
    private final HouseService houseService;

    @Nested
    class TestGetByUuid {

        @Test
        void getByUuidShouldReturnHouseResponseDto() throws Exception {
            HouseResponseDto expected = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();

            when(houseService.getHouseByUuid(expected.getUuid())).thenReturn(expected);

            mockMvc.perform(get("/houses/" + expected.getUuid()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.uuid", is(expected.getUuid().toString())))
                    .andExpect(jsonPath("$.area", is(expected.getArea())))
                    .andExpect(jsonPath("$.country", is(expected.getCountry())))
                    .andExpect(jsonPath("$.city", is(expected.getCity())))
                    .andExpect(jsonPath("$.street", is(expected.getStreet())))
                    .andExpect(jsonPath("$.number", is(expected.getNumber())))
                    .andExpect(jsonPath("$.createDate", is(expected.getCreateDate())));
//              .andExpect(content().json(objectMapper.writeValueAsString(expected)));

        }

        @Test
        void getByUuidShouldShouldThrowNotFound_whenInvalidUuid() throws Exception {
            UUID invalidUuid = UUID.randomUUID();
            String url = "/houses/" + invalidUuid;
            EntityNotFoundException exception = EntityNotFoundException.of(House.class, invalidUuid);

            when(houseService.getHouseByUuid(invalidUuid)).thenThrow(exception);

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

            HouseResponseDto houseTwo = HouseTestBuilderTwo.builder()
                    .build()
                    .buildHouseResponseDto();

            List<HouseResponseDto> expectedHouses = List.of(houseOne, houseTwo);
            when(houseService.getAllHouses(pageNumber, pageSize)).thenReturn(expectedHouses);

            // when & then
            mockMvc.perform(get("/houses")
                            .param("pageNumber", String.valueOf(pageNumber))
                            .param("pageSize", String.valueOf(pageSize)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expectedHouses)))

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

            verify(houseService, times(1)).saveHouse(requestDto);
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

            verify(houseService, times(1)).updateHouse(requestDto.getUuid(), requestDto);
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

            verify(houseService, times(1)).updateHouseFields(uuid, updates);
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

            verify(houseService, times(1)).deleteHouse(uuid);
        }
    }

    @Nested
    class TestGetTenants {

        @Test
        void getTenantsShouldReturnListOfPersonResponseDto() throws Exception {
            // given
            UUID uuid = TestConstant.HOUSE_ONE_UUID;
            PersonResponseDto tenantOne = PersonTestBuilder.builder().build().buildPersonResponseDto();
            PersonResponseDto tenantTwo = PersonTestBuilder.builder()
                    .withUuid(UUID.randomUUID())
                    .withName("Jane")
                    .withSurname("Doe")
                    .withSex(Sex.FEMALE)
                    .build()
                    .buildPersonResponseDto();

            List<PersonResponseDto> expectedTenants = List.of(tenantOne, tenantTwo);
            when(houseService.getTenantsByHouseUuid(uuid)).thenReturn(expectedTenants);

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
    }
}
