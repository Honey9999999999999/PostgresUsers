package org.example.pages;

import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;

public class Navigator {
    private static Navigator instance;

    private final Map<Type, Page> pageMap;
    private Page currentPage;

    private final ArrayDeque<Type> history = new ArrayDeque<>();

    private Navigator(){
        pageMap = new LinkedHashMap<>();
        pageMap.put(MainPage.class, new MainPage(this));
        pageMap.put(UserPage.class, new UserPage(this));
        pageMap.put(NewsPage.class, new NewsPage(this));
        pageMap.put(FriendsPage.class, new FriendsPage(this));
        pageMap.put(InRequestsPage.class, new InRequestsPage(this));

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
        history.push(currentPage.getClass());
        currentPage = pageMap.get(page);
        currentPage.onEnter();
        tryClearHistory();
    }

    public void update(){
        currentPage.onUpdate();
    }

    private void tryClearHistory(){
        if(!(currentPage instanceof BranchPage)){
            history.clear();
        }
    }

    public boolean isRunning(){
        return currentPage != null;
    }

    public void getBack(){
        currentPage.onExit();
        currentPage = pageMap.get(history.pop());
        currentPage.onEnter();
        tryClearHistory();
    }

    public void closeAll(){
        currentPage = null;
    }
}
