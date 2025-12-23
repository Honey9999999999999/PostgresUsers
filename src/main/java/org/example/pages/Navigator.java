package org.example.pages;

import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

public class Navigator {
    private static Navigator instance;

    private final Map<Type, Page> pageMap;
    private Page currentPage;

    private final Queue<Type> history = new ArrayDeque<>();

    private Navigator(){
        pageMap = new LinkedHashMap<>();
        pageMap.put(MainPage.class, new MainPage(this));
        pageMap.put(UserPage.class, new UserPage(this));
        pageMap.put(NewsPage.class, new NewsPage(this));
        pageMap.put(FriendsPage.class, new FriendsPage(this));

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

    public void addToHistory(Type page){
        history.add(page);
    }

    public void clearHistory(){
        history.clear();
    }

    public boolean isRunning(){
        return currentPage != null;
    }

    public void getBack(){
        enterIn(history.poll());
    }

    public void closeAll(){
        currentPage = null;
    }
}
