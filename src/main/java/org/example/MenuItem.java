package org.example;

import lombok.Getter;

@Getter

public class MenuItem {
    private final String title;
    private final Runnable action;

    public MenuItem(String title, Runnable action){
        this.title = title;
        this.action = action;
    }
}
