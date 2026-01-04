package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.UserDTO;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class) // Тестируем только слой контроллера
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService; // Заглушка сервиса

    @Autowired
    private ObjectMapper objectMapper; // Для превращения DTO в JSON

    @Test
    void shouldReturnAllUsers() throws Exception {
        UserDTO user = new UserDTO(1L, "Ivan", "ivan@example.com");
        when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ivan"))
                .andExpect(jsonPath("$[0].email").value("ivan@example.com"));
    }

    @Test
    void shouldCreateUser() throws Exception {
        UserDTO inputDto = new UserDTO(null, "New User", "new@example.com");
        UserDTO savedDto = new UserDTO(1L, "New User", "new@example.com");

        when(userService.save(any(UserDTO.class))).thenReturn(savedDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("New User"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserDTO updateDto = new UserDTO(null, "Updated Name", "upd@example.com");
        UserDTO resultDto = new UserDTO(1L, "Updated Name", "upd@example.com");

        when(userService.update(eq(1L), any(UserDTO.class))).thenReturn(resultDto);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
}