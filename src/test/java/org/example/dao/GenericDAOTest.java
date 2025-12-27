package org.example.dao;

import jakarta.persistence.PersistenceException;
import org.example.model.Password;
import org.example.model.Role;
import org.example.model.User;
import org.example.util.HibernateUtil;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class GenericDAOTest {
    private GenericDAO<User> userDao;
    private static Role defaultRole;

    @BeforeAll
    public static void init(){
        defaultRole = new Role();
        defaultRole.setName("USER");
        new GenericDAO<>(Role.class).save(defaultRole);
    }

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
        User user = new User("Ivan", "ivan@example.com", 54);
        user.setRole(defaultRole);
        userDao.save(user);

        assertNotNull(user.getId(), "ID должен быть сгенерирован");
        User found = userDao.findById(user.getId());
        assertEquals("Ivan", found.getName());
    }

    @Test
    @DisplayName("Должен выбрасывать исключение при сохранении без обязательного поля")
    void saveUserWithoutEmailThrowsException() {
        User user = new User("Ivan", null, 54);
        user.setRole(defaultRole);

        // Проверяем, что DAO выкидывает исключение при нарушении структуры БД
        assertThrows(PersistenceException.class, () -> userDao.save(user));
    }

    @Test
    @DisplayName("Должен возвращать список всех пользователей")
    void shouldFindAllUsers() {
        User user = new User("Danya", "danya@example.com", 23);
        user.setRole(defaultRole);
        userDao.save(user);
        user = new User("Ivan", "ivan@example.com", 54);
        user.setRole(defaultRole);
        userDao.save(user);

        List<User> users = userDao.findAll();

        assertEquals(2, users.size());
    }

    @Test
    @DisplayName("Должен удалить пользователя")
    void shouldDeleteUser() {
        User user = new User("Danya", "danya@example.com", 23);
        user.setRole(defaultRole);
        userDao.save(user);

        Long id = user.getId();
        user = userDao.findById(id);
        assertEquals("Danya", user.getName());

        userDao.delete(id);
        user = userDao.findById(id);
        assertNull(user);
    }

    @Test
    @DisplayName("Должен выбросить исключение из-за нарушения уникальности поля")
    void saveWithoutUnique() {
        User user1 = new User("Danya", "danya@example.com", 23);
        user1.setRole(defaultRole);
        userDao.save(user1);

        User user2 = new User("Ivan", "danya@example.com", 54);
        user2.setRole(defaultRole);

        assertThrows(PersistenceException.class, () -> userDao.save(user2));
    }

    @Test
    @DisplayName("Должен выбросить исключение из-за несуществующей роли")
    void saveUnexistRole() {
        User user = new User("Danya", "danya@example.com", 23);
        Role role = new Role();
        role.setId(5L);
        role.setName("GIGACHAD");
        user.setRole(role);

        assertThrows(PersistenceException.class, () -> userDao.save(user));
    }

    @Test
    @DisplayName("Должен выбросить исключение из-за невалидного пароля")
    void saveNoValidPassword() {
        User user = new User("Danya", "danya@example.com", 23);
        user.setRole(defaultRole);
        user.setPassword(new Password());

        assertThrows(PersistenceException.class, () -> userDao.save(user));
    }

    @Test
    @DisplayName("Должен упасть из-за ленивой инициализации")
    public void shouldThrowLazyEx(){
        User user = new User("Danya", "danya@example.com", 23);
        user.setRole(defaultRole);
        userDao.save(user);

        Long id = user.getId();
        User foundUser = userDao.findById(id);

        assertThrows(LazyInitializationException.class, () -> System.out.println(foundUser.getRole().getName()));
    }

    @Test
    @DisplayName("Должен сменить имя")
    public void shouldChangeName(){
        User user = new User("Danya", "danya@example.com", 23);
        user.setRole(defaultRole);
        userDao.save(user);

        Long id = user.getId();

        user.setName("Jonie");
        userDao.update(user);
        user = userDao.findById(id);

        assertEquals("Jonie", user.getName());
    }
}
