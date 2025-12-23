package org.example.pages;

import org.example.auth.AuthService;
import org.example.dao.UserDAO;
import org.example.model.User;

import java.util.LinkedHashMap;

public class MainPage extends Page {
    private final UserDAO userDAO;

    public MainPage(Navigator navigator) {
        super(navigator);
        userDAO = new UserDAO();
    }

    @Override
    public String getName() {
        return "Главное меню";
    }

    @Override
    protected LinkedHashMap<Integer, MenuItem> createMenu() {
        LinkedHashMap<Integer, MenuItem> menuMap = new LinkedHashMap<>();

        menuMap.put(1, new MenuItem("Войти в профиль", this::singIn));
        menuMap.put(2, new MenuItem("Создать пользователя", this::createUser));
        menuMap.put(3, new MenuItem("Найти пользователя по ID", this::findUserById));
        menuMap.put(4, new MenuItem("Показать всех пользователей", this::showAllUsers));
        menuMap.put(5, new MenuItem("Обновить пользователя", this::updateUser));
        menuMap.put(6, new MenuItem("Удалить пользователя", this::deleteUser));
        menuMap.put(0, new MenuItem("Выход", this::closeApp));

        return menuMap;
    }

    private void singIn(){
        System.out.print("Введите ID пользователя: ");

        if(AuthService.getInstance().login(scanner.nextLong())){
            navigator.enterIn(UserPage.class);
        }
        scanner.nextLine();
    }
    private void createUser(){
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
    private User findUserById(){
        System.out.print("Введите ID пользователя: ");
        Long id = scanner.nextLong();
        User user = userDAO.findById(id);
        System.out.println(user != null ? "Найдено: " + user.getName() : "Не найден");

        return user;
    }
    private void showAllUsers(){
        userDAO.findAll().forEach(u ->
                System.out.println("ID#" + u.getId() + ": " + u.getName() + " (" + u.getEmail() + ")"));
    }
    private void updateUser(){
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
    private void deleteUser(){
        System.out.print("Введите ID для удаления: ");
        userDAO.delete(scanner.nextLong());
        System.out.println("Готово.");
    }
    private void closeApp(){
        navigator.closeAll();
    }
}
