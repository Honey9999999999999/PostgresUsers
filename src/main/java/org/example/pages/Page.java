package org.example.pages;

import org.example.MenuItem;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public abstract class Page {
    protected Navigator navigator;
    protected Scanner scanner;

    protected String name;
    protected LinkedHashMap<Integer, MenuItem> menuMap;

    public Page(Navigator navigator){
        this.navigator = navigator;
        scanner = new Scanner(System.in);
        name = getName();
        menuMap = createMenu();
    }

    public abstract String getName();
    protected abstract LinkedHashMap <Integer, MenuItem> createMenu();

    public void onEnter(){}
    public void onUpdate(){ showMenu(); chooseInteraction(); }
    public void onExit(){}

    public void showMenu(){
        StringBuilder stringBuilder = new StringBuilder("\n").append("---").append(name).append("---");
        Set<Map.Entry<Integer, MenuItem>> menuItems = menuMap.entrySet();

        for(var item : menuItems){
            stringBuilder.append("\n").append(item.getKey()).append(": ").append(item.getValue().getTitle());
        }
        stringBuilder.append("\nВыберите действие: ");

        System.out.print(stringBuilder);
    }

    public void chooseInteraction(){
        menuMap.get(scanner.nextInt()).getAction().run();
    }
}
