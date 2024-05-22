package com.gmail.merikbest2015.ecommerce.controller;

import org.example.OnlineShop.constants.ErrorMessage;
import org.example.OnlineShop.constants.Pages;
import org.example.OnlineShop.constants.PathConstants;
import org.example.OnlineShop.constants.SuccessMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.gmail.merikbest2015.ecommerce.util.TestConstants.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WithUserDetails(ADMIN_EMAIL)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/create-phones-before.sql", "/sql/create-user-before.sql", "/sql/create-orders-before.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/create-orders-after.sql", "/sql/create-user-after.sql", "/sql/create-phones-after.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("[200] GET /admin/phones - Get Phones")
    public void getPhones() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/phones"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_PHONES))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(12))));
    }

    @Test
    @DisplayName("[200] GET /admin/phones/search - Search Phones By brand")
    public void searchPhones_ByPhoner() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/phones/search")
                        .param("searchType", "brand")
                        .param("text", "Creed"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_PHONES))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(7))));
    }

    @Test
    @DisplayName("[200] GET /admin/phones/search - Search Phones By country")
    public void searchPhones_ByCountry() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/phones/search")
                        .param("searchType", "country")
                        .param("text", "Spain"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_PHONES))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(2))));
    }

    @Test
    @DisplayName("[200] GET /admin/phones/search - Search Phones By phoneTitle")
    public void searchPhones_PhoneTitle() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/phones/search")
                        .param("searchType", "phoneTitle")
                        .param("text", "Aventus"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_PHONES))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(2))));
    }

    @Test
    @DisplayName("[200] GET /admin/phones/search - Get Users")
    public void getUsers() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/users"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ALL_USERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(3))));
    }

    @Test
    @DisplayName("[200] GET /admin/users/search - Search Users By email")
    public void searchUsers_ByEmail() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/users/search")
                        .param("searchType", "email")
                        .param("text", USER_EMAIL))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ALL_USERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[200] GET /admin/users/search - Search Users By First Name")
    public void searchUsers_ByFirstName() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/users/search")
                        .param("searchType", "firstName")
                        .param("text", USER_FIRST_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ALL_USERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(2))));
    }

    @Test
    @DisplayName("[200] GET /admin/users/search - Search Users By Last Name")
    public void searchUsers_ByLastName() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/users/search")
                        .param("searchType", "lastName")
                        .param("text", USER_LAST_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ALL_USERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(2))));
    }

    @Test
    @DisplayName("[200] GET /admin/order/111 - Get Order")
    public void getOrder() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/order/{orderId}", 111))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ORDER))
                .andExpect(model().attribute("order", hasProperty("id", is(ORDER_ID))))
                .andExpect(model().attribute("order", hasProperty("totalPrice", is(ORDER_TOTAL_PRICE))))
                .andExpect(model().attribute("order", hasProperty("firstName", is(ORDER_FIRST_NAME))))
                .andExpect(model().attribute("order", hasProperty("lastName", is(ORDER_LAST_NAME))))
                .andExpect(model().attribute("order", hasProperty("city", is(ORDER_CITY))))
                .andExpect(model().attribute("order", hasProperty("address", is(ORDER_ADDRESS))))
                .andExpect(model().attribute("order", hasProperty("email", is(ORDER_EMAIL))))
                .andExpect(model().attribute("order", hasProperty("phoneNumber", is(ORDER_PHONE_NUMBER))))
                .andExpect(model().attribute("order", hasProperty("postIndex", is(ORDER_POST_INDEX))))
                .andExpect(model().attribute("order", hasProperty("phones", hasSize(2))));
    }

    @Test
    @DisplayName("[200] GET /admin/orders - Get Orders")
    public void getOrders() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ORDERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[200] GET /admin/orders/search - Search Orders By Email")
    public void searchOrders_ByEmail() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/orders/search")
                        .param("searchType", "email")
                        .param("text", USER_EMAIL))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ORDERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[200] GET /admin/orders/search - Search Orders bt First Name")
    public void searchOrders_ByFirstName() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/orders/search")
                        .param("searchType", "firstName")
                        .param("text", USER_FIRST_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ORDERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[200] GET /admin/orders/search - Search Orders By Last Name")
    public void searchOrders_ByLastName() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/orders/search")
                        .param("searchType", "lastName")
                        .param("text", USER_LAST_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ORDERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[200] GET /admin/phone/1 - Get Phone")
    public void getPhone() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/phone/{phoneId}", PHONE_ID))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_EDIT_PHONE))
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
    @DisplayName("[404] GET /admin/phone/111 - Get Phone Not Found")
    public void getPhone_NotFound() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/phone/{phoneId}", 111))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(ErrorMessage.PHONE_NOT_FOUND));
    }

    @Test
    @DisplayName("[300] POST /admin/edit/phone - Edit Phone")
    public void editPhone() throws Exception {
        mockMvc.perform(multipart(PathConstants.ADMIN + "/edit/phone")
                        .file(mockFile())
                        .param("id", String.valueOf(PHONE_ID))
                        .param("phoneTitle", PHONE_TITLE)
                        .param("brand", brand)
                        .param("year", String.valueOf(YEAR))
                        .param("country", COUNTRY)
                        .param("phoneGender", PHONE_GENDER)
                        .param("fragranceTopNotes", FRAGRANCE_TOP_NOTES)
                        .param("fragranceMiddleNotes", FRAGRANCE_MIDDLE_NOTES)
                        .param("fragranceBaseNotes", FRAGRANCE_BASE_NOTES)
                        .param("price", String.valueOf(PRICE))
                        .param("volume", VOLUME)
                        .param("type", TYPE))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/phones"))
                .andExpect(flash().attribute("messageType", "alert-success"))
                .andExpect(flash().attribute("message", SuccessMessage.PHONE_EDITED));
    }

    @Test
    @DisplayName("[200] POST /admin/edit/phone - Edit Phone Return Input Errors")
    public void editPhone_ReturnInputErrors() throws Exception {
        mockMvc.perform(multipart(PathConstants.ADMIN + "/edit/phone")
                        .file(mockFile())
                        .param("id", String.valueOf(PHONE_ID))
                        .param("phoneTitle", "")
                        .param("brand", "")
                        .param("year", "0")
                        .param("country", "")
                        .param("phoneGender", "")
                        .param("fragranceTopNotes", "")
                        .param("fragranceMiddleNotes", "")
                        .param("fragranceBaseNotes", "")
                        .param("price", "0")
                        .param("volume", "")
                        .param("type", ""))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_EDIT_PHONE))
                .andExpect(model().attribute("phoneTitleError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("brandError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("yearError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("countryError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("phoneGenderError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("fragranceTopNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("fragranceMiddleNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("fragranceBaseNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("priceError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("volumeError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("typeError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)));
    }

    @Test
    @DisplayName("[200] GET /admin/add/phone - Get Add Phone Page")
    public void getAddPhonePage() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/add/phone"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ADD_PHONE));
    }

    @Test
    @DisplayName("[300] POST /admin/add/phone - Add Phone")
    public void addPhone() throws Exception {
        mockMvc.perform(multipart(PathConstants.ADMIN + "/add/phone")
                        .file(mockFile())
                        .param("phoneTitle", PHONE_TITLE)
                        .param("brand", brand)
                        .param("year", String.valueOf(YEAR))
                        .param("country", COUNTRY)
                        .param("phoneGender", PHONE_GENDER)
                        .param("fragranceTopNotes", FRAGRANCE_TOP_NOTES)
                        .param("fragranceMiddleNotes", FRAGRANCE_MIDDLE_NOTES)
                        .param("fragranceBaseNotes", FRAGRANCE_BASE_NOTES)
                        .param("price", String.valueOf(PRICE))
                        .param("volume", VOLUME)
                        .param("type", TYPE))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/phones"))
                .andExpect(flash().attribute("messageType", "alert-success"))
                .andExpect(flash().attribute("message", SuccessMessage.PHONE_ADDED));
    }

    @Test
    @DisplayName("[200] POST /admin/add/phone - Add Phone Return Input Errors")
    public void addPhone_ReturnInputErrors() throws Exception {
        mockMvc.perform(multipart(PathConstants.ADMIN + "/add/phone")
                        .file(mockFile())
                        .param("phoneTitle", "")
                        .param("brand", "")
                        .param("year", "0")
                        .param("country", "")
                        .param("phoneGender", "")
                        .param("fragranceTopNotes", "")
                        .param("fragranceMiddleNotes", "")
                        .param("fragranceBaseNotes", "")
                        .param("price", "0")
                        .param("volume", "")
                        .param("type", ""))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ADD_PHONE))
                .andExpect(model().attribute("phoneTitleError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("brandError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("yearError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("countryError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("phoneGenderError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("fragranceTopNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("fragranceMiddleNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("fragranceBaseNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("priceError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("volumeError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("typeError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)));
    }

    @Test
    @DisplayName("[200] GET /admin/user/122 - Get User By Id")
    public void getUserById() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/user/{phoneId}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_USER_DETAIL))
                .andExpect(model().attribute("user", hasProperty("id", is(USER_ID))))
                .andExpect(model().attribute("user", hasProperty("email", is(USER_EMAIL))))
                .andExpect(model().attribute("user", hasProperty("firstName", is(USER_FIRST_NAME))))
                .andExpect(model().attribute("user", hasProperty("lastName", is(USER_LAST_NAME))))
                .andExpect(model().attribute("user", hasProperty("city", is(USER_CITY))))
                .andExpect(model().attribute("user", hasProperty("address", is(USER_ADDRESS))))
                .andExpect(model().attribute("user", hasProperty("phoneNumber", is(USER_PHONE_NUMBER))))
                .andExpect(model().attribute("user", hasProperty("postIndex", is(USER_POST_INDEX))))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[404] GET /admin/user/123 - Get User By Id Not Found")
    public void getUserById_NotFound() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/user/{phoneId}", 123))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(ErrorMessage.USER_NOT_FOUND));
    }

    private MockMultipartFile mockFile() throws IOException {
        FileInputStream inputFile = new FileInputStream(new File(FILE_PATH));
        return new MockMultipartFile("file", FILE_NAME, MediaType.MULTIPART_FORM_DATA_VALUE, inputFile);
    }
}
