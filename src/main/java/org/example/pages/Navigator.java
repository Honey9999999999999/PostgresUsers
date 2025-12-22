package org.example.pages;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class Navigator {
    private static Navigator instance;

    private final Map<Type, Page> pageMap;
    private Page currentPage;

    private Navigator(){
        pageMap = new LinkedHashMap<>();
        pageMap.put(MainPage.class, new MainPage(this));
        pageMap.put(UserPage.class, new UserPage(this));

        currentPage = pageMap.get(MainPage.class);
        currentPage.onEnter();
    }

    public static synchronized Navigator getInstance() {
        if (instance == null) {
            instance = new Navigator();
        }
        return instance;
    }

    public <T extends Type> void enterIn(T page){
        currentPage.onExit();
        currentPage = pageMap.get(page);
        currentPage.onEnter();
    }

    public void update(){
        currentPage.onUpdate();
    }

    public boolean isRunning(){
        return currentPage != null;
    }

    public void closeAll(){
        currentPage = null;
    }
}
