package com.gmail.merikbest2015.ecommerce.controller;

import org.example.OnlineShop.constants.ErrorMessage;
import org.example.OnlineShop.constants.Pages;
import org.example.OnlineShop.constants.PathConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.merikbest2015.ecommerce.util.TestConstants.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/create-phones-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/create-phones-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PhoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("[200] GET /phone/1 - Get Phones")
    public void getPhoneById() throws Exception {
        mockMvc.perform(get(PathConstants.PHONE + "/{phoneId}", PHONE_ID))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.PHONE))
                .andExpect(model().attribute("phone", hasProperty("id", is(PHONE_ID))))
                .andExpect(model().attribute("phone", hasProperty("phoneTitle", is(PHONE_TITLE))))
                .andExpect(model().attribute("phone", hasProperty("brand", is(brand))))
                .andExpect(model().attribute("phone", hasProperty("year", is(YEAR))))
                .andExpect(model().attribute("phone", hasProperty("country", is(COUNTRY))))
                .andExpect(model().attribute("phone", hasProperty("phoneGender", is(PHONE_GENDER))))
                .andExpect(model().attribute("phone", hasProperty("fragranceTopNotes", is(FRAGRANCE_TOP_NOTES))))
                .andExpect(model().attribute("phone", hasProperty("fragranceMiddleNotes", is(FRAGRANCE_MIDDLE_NOTES))))
                .andExpect(model().attribute("phone", hasProperty("fragranceBaseNotes", is(FRAGRANCE_BASE_NOTES))))
                .andExpect(model().attribute("phone", hasProperty("filename", is(FILENAME))))
                .andExpect(model().attribute("phone", hasProperty("price", is(PRICE))))
                .andExpect(model().attribute("phone", hasProperty("volume", is(VOLUME))))
                .andExpect(model().attribute("phone", hasProperty("type", is(TYPE))));
    }

    @Test
    @DisplayName("[404] GET /phone/111 - Get Phone By Id NotFound")
    public void getPhoneById_NotFound() throws Exception {
        mockMvc.perform(get(PathConstants.PHONE + "/{phoneId}", 111))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(ErrorMessage.PHONE_NOT_FOUND));
    }

    @Test
    @DisplayName("[200] GET /phone - Get Phones By Filter Params")
    public void getPhonesByFilterParams() throws Exception {
        mockMvc.perform(get(PathConstants.PHONE))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.PHONES))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(12))));
    }

    @Test
    @DisplayName("[200] GET /phone - Get Phones By Filter Params: brands")
    public void getPhonesByFilterParams_Phoners() throws Exception {
        mockMvc.perform(get(PathConstants.PHONE)
                        .param("brands", "Creed"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.PHONES))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(7))));
    }

    @Test
    @DisplayName("[200] GET /phone - Get Phones By Filter Params: brands, genders")
    public void getPhonesByFilterParams_PhonersAndGenders() throws Exception {
        mockMvc.perform(get(PathConstants.PHONE)
                        .param("brands", "Creed")
                        .param("genders", "male"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.PHONES))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(3))));
    }

    @Test
    @DisplayName("[200] GET /phone - Get Phones By Filter Params: brands, genders, price")
    public void getPhonesByFilterParams_PhonersAndGendersAndPrice() throws Exception {
        mockMvc.perform(get(PathConstants.PHONE)
                        .param("brands", "Creed", "Dior")
                        .param("genders", "male")
                        .param("price", "60"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.PHONES))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(5))));
    }

    @Test
    @DisplayName("[200] GET /phone/search - Search Phones By brand")
    public void searchPhones_ByPhoner() throws Exception {
        mockMvc.perform(get(PathConstants.PHONE + "/search")
                        .param("searchType", "brand")
                        .param("text", "Creed"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.PHONES))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(7))));
    }

    @Test
    @DisplayName("[200] GET /phone/search - Search Phones By Country")
    public void searchPhones_ByCountry() throws Exception {
        mockMvc.perform(get(PathConstants.PHONE + "/search")
                        .param("searchType", "country")
                        .param("text", "Spain"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.PHONES))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(2))));
    }

    @Test
    @DisplayName("[200] GET /phone/search - Search Phones By Phone Title")
    public void searchPhones_PhoneTitle() throws Exception {
        mockMvc.perform(get(PathConstants.PHONE + "/search")
                        .param("searchType", "phoneTitle")
                        .param("text", "Aventus"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.PHONES))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(2))));
    }
}
