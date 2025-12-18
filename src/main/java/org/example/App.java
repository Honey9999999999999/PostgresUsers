package org.example;

import org.example.dao.UserDAO;
import org.example.model.User;

import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        UserDAO userDAO = new UserDAO();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String menuText = "\n--- Меню пользователя ---\n" +
                    "1. Создать пользователя\n" +
                    "2. Найти пользователя по ID\n" +
                    "3. Показать всех пользователей\n" +
                    "4. Обновить пользователя\n" +
                    "5. Удалить пользователя\n" +
                    "0. Выход\n" +
                    "Выберите действие: ";
            System.out.print(menuText);

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) break;

            switch (choice) {
                case 1 -> {
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
                case 2 -> {
                    System.out.print("Введите ID: ");
                    Long id = scanner.nextLong();
                    User user = userDAO.findById(id);
                    System.out.println(user != null ? "Найдено: " + user.getName() : "Не найден");
                }
                case 3 -> {
                    userDAO.findAll().forEach(u ->
                            System.out.println(u.getId() + ": " + u.getName() + " (" + u.getEmail() + ")"));
                }
                case 4 -> {
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
                case 5 -> {
                    System.out.print("Введите ID для удаления: ");
                    userDAO.delete(scanner.nextLong());
                    System.out.println("Готово.");
                }
            }
        }
        System.out.println("Программа завершена.");
    }
}
