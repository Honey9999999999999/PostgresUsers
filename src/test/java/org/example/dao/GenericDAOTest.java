package org.example.dao;

import org.example.model.Role;
import org.example.model.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers // Активирует расширение Testcontainers
class GenericDAOTest {
    private GenericDAO<User> userDao;

    @BeforeEach
    void setUp() {
        // Инициализируем DAO перед каждым тестом
        userDao = new GenericDAO<>(User.class);

        // Очищаем таблицу перед каждым тестом для изоляции
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Должен успешно сохранять и находить пользователя по ID")
    void shouldSaveAndFindUser() {
        Role role = new Role();
        role.setName("USER");

        new GenericDAO<>(Role.class).save(role);

        User user = new User();
        user.setName("Ivan");
        user.setEmail("dsf");
        user.setAge(23);
        user.setRole(role);

        userDao.save(user);

        assertNotNull(user.getId(), "ID должен быть сгенерирован");
        User found = userDao.findById(user.getId());
        assertEquals("Ivan", found.getName());
    }

//    @Test
//    @DisplayName("Должен возвращать список всех пользователей")
//    void shouldFindAllUsers() {
//        User user = new User();
//        user.setName("User 1");
//        userDao.save(user);
//        user.setName("User 2");
//        userDao.save(user);
//
//        List<User> users = userDao.findAll();
//
//        assertEquals(2, users.size());
//    }
}
