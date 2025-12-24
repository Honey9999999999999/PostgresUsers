package org.example.pages;

import org.example.util.SafeScanner;

import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class Page {
    protected Navigator navigator;
    protected Scanner scanner;
    protected SafeScanner safeScanner;

    protected String name;
    protected String body = "";
    private boolean isSetBody;
    protected LinkedHashMap<Integer, MenuItem> menuMap;
    protected final Queue<String> messages = new ArrayDeque<>();

    public Page(Navigator navigator){
        this.navigator = navigator;
        scanner = new Scanner(System.in, System.console() != null ? System.console().charset() : StandardCharsets.UTF_8);
        safeScanner = new SafeScanner(scanner);
        name = getName();
        menuMap = createMenu();
    }

    public abstract String getName();
    protected abstract LinkedHashMap <Integer, MenuItem> createMenu();

    public void onEnter(){ body = getBody(); }
    public void onUpdate(){ showPage(); chooseInteraction(); }
    public void onExit(){ isSetBody = false; }

    private void showPage(){
        showHeader();
        setBody();
        if(!body.isEmpty()){
            System.out.println(body);
        }
        showMessages();
        showMenu();
    }
    private void showHeader(){
        System.out.print("\n\n---" + name + "---\n");
    }
    protected String getBody(){ return  ""; }
    private void setBody(){
        if(!isSetBody){
            body = getBody();
            isSetBody = true;
        }
    }
    private void showMessages(){
        StringJoiner joiner = new StringJoiner("\n");
        while (!messages.isEmpty()){
            joiner.add(messages.poll());
        }
        System.out.print(joiner);
    }
    private void showMenu(){
        StringBuilder stringBuilder = new StringBuilder();
        Set<Map.Entry<Integer, MenuItem>> menuItems = menuMap.entrySet();

        for(var item : menuItems){
            stringBuilder.append("\n").append(item.getKey()).append(": ").append(item.getValue().title());
        }
        stringBuilder.append("\nВыберите действие: ");

        System.out.print(stringBuilder);
    }

    public void chooseInteraction(){
        if (!scanner.hasNextInt()) {
            System.out.println("Ошибка: введите число!");
            scanner.nextLine();
            return;
        }

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (menuMap.containsKey(choice)) {
            menuMap.get(choice).action().run();
        } else {
            System.out.println("Такого пункта меню нет!");
        }
    }
}
