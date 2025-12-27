package org.example.service;

import org.example.dao.GenericDAO;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private GenericDAO<User> userDAO;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        // Инициализируем сервис перед каждым тестом (изоляция состояния)
        userService = new UserService(userDAO);
    }

    @Test
    @DisplayName("Должен успешно сохранять пользователя с ролью по умолчанию")
    void createNewUser_Success() {
        User user = new User("Ivan", "ivan@example.com", 54);

        userService.createNewUser(user);

        // Проверяем взаимодействие только с этим объектом
        verify(userDAO, times(1)).save(user);
        assertEquals(3L, user.getRole().getId());
        verifyNoMoreInteractions(userDAO);
    }

    @Test
    @DisplayName("Должен корректно обновлять имя существующего пользователя")
    void updateUserName_UserExists_Success() {
        // Arrange (Подготовка)
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("OldName");

        when(userDAO.findById(userId)).thenReturn(existingUser);

        // Act (Действие)
        userService.updateUserName(userId, "NewName");

        // Assert (Проверка)
        assertEquals("NewName", existingUser.getName());
        verify(userDAO).findById(userId);
        verify(userDAO).update(existingUser);
        verifyNoMoreInteractions(userDAO);
    }

    @Test
    @DisplayName("Должен выбрасывать исключение, если пользователь для обновления не найден")
    void updateUserName_UserNotFound_ThrowsException() {
        Long userId = 99L;
        when(userDAO.findById(userId)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUserName(userId, "SomeName");
        });

        assertTrue(exception.getMessage().contains(String.valueOf(userId)));
        // Проверяем, что метод update НЕ вызывался
        verify(userDAO, never()).update(any());
    }

    @Test
    @DisplayName("Должен выбрасывать исключение, если пользователь без почты")
    public void saveUserWithoutEmail(){
        User user = new User();
        user.setName("Danya");
        user.setAge(67);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createNewUser(user);
        });

        verify(userDAO, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Должен возвращать список всех пользователей")
    void findAllUsers_ShouldReturnList() {
        User user1 = new User();
        user1.setName("User1");
        User user2 = new User();
        user2.setName("User2");
        List<User> userList = List.of(user1, user2);

        when(userDAO.findAll()).thenReturn(userList);

        List<User> result = userService.findAllUsers();

        assertEquals(2, result.size());
        assertEquals("User1", result.get(0).getName());
        verify(userDAO).findAll();
        verifyNoMoreInteractions(userDAO);
    }

    @Test
    @DisplayName("Поиск по ID должен возвращать пользователя")
    void findById_Success() {
        User user = new User();
        user.setId(10L);
        when(userDAO.findById(10L)).thenReturn(user);

        User result = userService.findById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(userDAO).findById(10L);
    }
}
