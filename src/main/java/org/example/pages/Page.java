package org.example.pages;

import java.util.*;

public abstract class Page {
    protected Navigator navigator;
    protected Scanner scanner;

    protected String name;
    protected LinkedHashMap<Integer, MenuItem> menuMap;
    protected final Queue<String> messages = new ArrayDeque<>();

    public Page(Navigator navigator){
        this.navigator = navigator;
        scanner = new Scanner(System.in);
        name = getName();
        menuMap = createMenu();
    }

    public abstract String getName();
    protected abstract LinkedHashMap <Integer, MenuItem> createMenu();

    public void onEnter(){}
    public void onUpdate(){ showPage(); chooseInteraction(); }
    public void onExit(){}

    private void showPage(){
        showHeader();
        String body = getBody();
        if(!body.isEmpty())
            System.out.print(body);
        showMessages();
        showMenu();
    }
    private void showHeader(){
        System.out.print("\n\n---" + name + "---\n");
    }
    protected String getBody(){ return  ""; }
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
            stringBuilder.append("\n").append(item.getKey()).append(": ").append(item.getValue().getTitle());
        }
        stringBuilder.append("\nВыберите действие: ");

        System.out.print(stringBuilder);
    }

    public void chooseInteraction(){
        menuMap.get(scanner.nextInt()).getAction().run();
    }
}
