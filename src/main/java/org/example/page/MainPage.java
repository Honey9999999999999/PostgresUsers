package org.example.page;

import lombok.extern.slf4j.Slf4j;
import org.example.auth.AuthService;
import org.example.model.Password;
import org.example.model.User;
import org.example.service.UserService;
import java.util.LinkedHashMap;

@Slf4j
public class MainPage extends Page {

    public MainPage(Navigator navigator) {
        super(navigator);
    }

    @Override
    public String getName() {
        return "Главное меню";
    }

    @Override
    protected LinkedHashMap<Integer, MenuItem> createMenu() {
        LinkedHashMap<Integer, MenuItem> menuMap = new LinkedHashMap<>();

        menuMap.put(1, new MenuItem("Войти в профиль", this::singIn));
        menuMap.put(2, new MenuItem("Зарегистрироваться", this::singUp));
        menuMap.put(0, new MenuItem("Выход", this::closeApp));

        return menuMap;
    }

    private void singIn(){
        System.out.print("Введите ID пользователя: ");
        Long id = safeScanner.nextNumber(Long.class, 1L, Long.MAX_VALUE);
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        if(AuthService.getInstance().login(id, password)){
            navigator.enterIn(UserPage.class);
            System.out.print("Успешно!");
            return;
        }
        System.out.print("Неправильные логин или пароль!");
    }
    private void singUp(){
        User user = new User();
        System.out.print("Введите имя: ");
        user.setName(scanner.nextLine());
        System.out.print("Введите email: ");
        user.setEmail(scanner.nextLine());
        System.out.print("Введите возраст: ");
        user.setAge(safeScanner.nextNumber(Integer.class, 0, 100));
        System.out.print("Введите пароль: ");
        String pass = scanner.nextLine();
        Password password = new Password(user, pass);
        user.setPassword(password);

        //userService.createNewUser(user);
        System.out.print("Пользователь сохранен!");
    }
    private void closeApp(){
        navigator.closeAll();
    }
}
