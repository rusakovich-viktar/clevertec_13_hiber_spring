package by.clevertec.house.controller;

import static by.clevertec.house.util.TestConstant.PAGE_NUMBER_SHOULD_BE_POSITIVE;
import static by.clevertec.house.util.TestConstant.PAGE_SIZE;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.clevertec.house.dto.PersonResponseDto;
import by.clevertec.house.service.PersonService;
import by.clevertec.house.util.PersonTestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@RequiredArgsConstructor
@WebMvcTest(PersonController.class)
class PersonControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final UUID randomUUID = UUID.randomUUID();

    @MockBean
    private final PersonService personService;

    @Test
    public void getPersonByUuidShouldReturnPersonResponseDto() throws Exception {
        // given

        PersonResponseDto expected = PersonTestBuilder.builder()
                .build()
                .buildPersonResponseDto();

        when(personService.getPersonByUuid(randomUUID)).thenReturn(expected);

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
    public void getAllPersonsShouldReturnListOfPersons() throws Exception {
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
        when(personService.getAllPersons(pageNumber, pageSize)).thenReturn(expectedPersons);

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
