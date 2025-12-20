package org.example;

import org.example.dao.PostDAO;
import org.example.dao.UserDAO;
import org.example.model.Post;
import org.example.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App 
{
    private static UserDAO userDAO;
    private static PostDAO postDAO;
    private static Scanner scanner;

    private static final HashMap<PageType, String> menusMap = new HashMap<>(Map.of(
        PageType.Main, "\n--- Главное меню ---\n" +
                "1. Войти в профиль\n" +
                "2. Создать пользователя\n" +
                "3. Найти пользователя по ID\n" +
                "4. Показать всех пользователей\n" +
                "5. Обновить пользователя\n" +
                "6. Удалить пользователя\n" +
                "0. Выход\n" +
                "Выберите действие: ",
        PageType.User, "\n--- Меню пользователя ---\n" +
                "1. Написать пост\n" +
                "2. Показать все посты\n" +
                "0. Выйти из профиля\n" +
                "Выберите действие: "
        )
    );

    private static final HashMap<PageType, HashMap<Integer, Runnable>> actionsMap = new HashMap<>(Map.of(
        PageType.Main, new HashMap<>(Map.of(
            1, App::singIn,
            2, App::createUser,
            3, App::findUserById,
            4, App::showAllUsers,
            5, App::updateUser,
            6, App::deleteUser,
            0, App::closeApp
        )),
        PageType.User, new HashMap<>(Map.of(
            1, App::createPost,
            2, App::getPosts,
            0, App::singOut
        ))
    ));

    private static PageType currentPage = PageType.Main;
    private static User currentUser;

    private static Boolean isRunning = true;

    public static void main( String[] args )
    {
        initialize();

        while (isRunning) {
            System.out.print(menusMap.get(currentPage));
            int choice = scanner.nextInt();
            scanner.nextLine();
            actionsMap.get(currentPage).get(choice).run();
        }

        System.out.println("Программа завершена.");
    }

    private static void initialize() {
        userDAO = new UserDAO();
        postDAO = new PostDAO();
        scanner = new Scanner(System.in);
    }

    private static void singIn(){
        currentUser = findUserById();
        currentPage = currentUser != null ? PageType.User : PageType.Main;
    }
    private static void createUser(){
        User user = new User();
        System.out.print("Введите имя: ");
        user.setName(scanner.nextLine());
        System.out.print("Введите email: ");
        user.setEmail(scanner.nextLine());
        System.out.print("Введите возраст: ");
        user.setAge(scanner.nextInt());
        userDAO.save(user);
        System.out.println("Пользователь сохранен!");
    }
    private static User findUserById(){
        System.out.print("Введите ID: ");
        Long id = scanner.nextLong();
        User user = userDAO.findById(id);
        System.out.println(user != null ? "Найдено: " + user.getName() : "Не найден");

        return user;
    }
    private static void showAllUsers(){
        userDAO.findAll().forEach(u ->
                System.out.println(u.getId() + ": " + u.getName() + " (" + u.getEmail() + ")"));
    }
    private static void updateUser(){
        System.out.print("Введите ID пользователя: ");
        User user = userDAO.findById(scanner.nextLong());
        scanner.nextLine();

        System.out.print("Введите новое имя: ");
        user.setName(scanner.nextLine());
        System.out.print("Введите новый email: ");
        user.setEmail(scanner.nextLine());
        System.out.print("Введите новый возраст: ");
        user.setAge(scanner.nextInt());
        userDAO.update(user);
        System.out.println("Пользователь обновлен!");
    }
    private static void deleteUser(){
        System.out.print("Введите ID для удаления: ");
        userDAO.delete(scanner.nextLong());
        System.out.println("Готово.");
    }
    private static void closeApp(){
        isRunning = false;
    }

    private static void createPost(){
        Post post = new Post();
        System.out.print("Введите название поста: ");
        post.setName(scanner.nextLine());
        System.out.print("Введите содержание: ");
        post.setContent(scanner.nextLine());

        userDAO.createPost(currentUser.getId(), post);
    }
    private static void getPosts(){
        postDAO.getPostsByID(currentUser.getId()).forEach(p ->
                System.out.println(p.getName() + " : " + p.getContent()));
    }
    private static void singOut(){
        currentUser = null;
        currentPage = PageType.Main;
    }
}
