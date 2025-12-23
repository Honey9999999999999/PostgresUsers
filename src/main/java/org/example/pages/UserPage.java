package org.example.pages;

import org.example.auth.AuthService;
import org.example.model.*;
import org.example.util.DataBaseServices;

import java.util.LinkedHashMap;

public class UserPage extends Page {
    private User currentUser;

    public UserPage(Navigator navigator) {
        super(navigator);
    }

    @Override
    public String getName() {
        return "Страница пользователя";
    }

    @Override
    protected LinkedHashMap<Integer, MenuItem> createMenu() {
        LinkedHashMap<Integer, MenuItem> menuMap = new LinkedHashMap<>();

        menuMap.put(1, new MenuItem("Открыть \"Новости\"", () -> navigator.enterIn(NewsPage.class)));
        menuMap.put(2, new MenuItem("Открыть \"Друзья\"", () -> navigator.enterIn(FriendsPage.class)));
        menuMap.put(0, new MenuItem("Выйти из профиля", this::singOut));

        return menuMap;
    }

    @Override
    public void onEnter(){
        super.onEnter();
        currentUser = DataBaseServices.getInstance().userDAO.findById(AuthService.getInstance().getCurrentUserId());
    }

    @Override
    public void onExit(){
        super.onExit();
        currentUser = null;
    }

    private void singOut(){
        navigator.enterIn(MainPage.class);
    }
}
