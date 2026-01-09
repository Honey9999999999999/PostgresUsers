package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.rest.UserController;
import org.example.dto.UserDTO;
import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService; // Заглушка сервиса

    @Autowired
    private ObjectMapper objectMapper; // Для превращения DTO в JSON

    @Test
    void shouldReturnUser() throws Exception {
        UserDTO user = new UserDTO(1L, "Ivan", "ivan@mail.ru", 23);
        when(userService.findById(eq(1L))).thenReturn(user);

        MvcResult result = mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        user = objectMapper.readValue(jsonResponse, UserDTO.class);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("Ivan");
        assertThat(user.getEmail()).contains("@mail.ru");
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        UserDTO user = new UserDTO(1L, "Ivan", "ivan@example.com", 23);
        when(userService.findAll()).thenReturn(List.of(user));

        MvcResult result = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        List<UserDTO> users = objectMapper.readValue(
                jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, UserDTO.class)
        );

        assertThat(users.get(0).getName()).isEqualTo("Ivan");
        assertThat(users.get(0).getEmail()).isEqualTo("ivan@example.com");
    }

    @Test
    void shouldCreateUser() throws Exception {
        UserDTO inputDto = new UserDTO(null, "New User", "new@example.com", 23);
        UserDTO savedDto = new UserDTO(1L, "New User", "new@example.com", 23);

        when(userService.save(any(UserDTO.class))).thenReturn(savedDto);

        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        UserDTO user = objectMapper.readValue(jsonResponse, UserDTO.class);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("New User");
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserDTO updateDto = new UserDTO(null, "Updated Name", "upd@example.com", 23);
        UserDTO resultDto = new UserDTO(1L, "Updated Name", "upd@example.com", 23);

        when(userService.update(eq(1L), any(UserDTO.class))).thenReturn(resultDto);

        MvcResult result = mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        UserDTO user = objectMapper.readValue(jsonResponse, UserDTO.class);

        assertThat(user.getName()).isEqualTo("Updated Name");
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
}