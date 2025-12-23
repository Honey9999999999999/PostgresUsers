package org.example.pages;

import org.example.auth.AuthService;
import org.example.model.Message;
import org.example.model.User;
import org.example.util.DataBaseServices;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringJoiner;

public class ChatPage extends BranchPage{
    private User currentUser;
    private User friend;

    public ChatPage(Navigator navigator) {
        super(navigator);
    }

    @Override
    public String getName() {
        return "Переписка";
    }

    @Override
    protected LinkedHashMap<Integer, MenuItem> createMenu() {
        LinkedHashMap<Integer, MenuItem> menuMap = new LinkedHashMap<>();

        menuMap.put(1, new MenuItem("Отправить сообщение", this::sendMessage));

        return menuMap;
    }

    @Override
    public void onEnter(){
        currentUser = DataBaseServices.getInstance().userDAO.findById(AuthService.getInstance().getCurrentUserId());
        chooseFriend();
    }

    @Override
    protected String getBody(){
        List<Message> messages = DataBaseServices.getInstance().messageDAO.getMessages(currentUser.getId(), friend.getId());
        StringJoiner stringJoiner = new StringJoiner("\n");

        for(Message message : messages){
            stringJoiner.add(message.getSender().getName() + " говорит: " + message.getText());
        }

        return stringJoiner.toString();
    }

    private void chooseFriend(){
        List<User> friends = DataBaseServices.getInstance().friendShipDAO.getFriends(AuthService.getInstance().getCurrentUserId());

        StringJoiner stringJoiner = new StringJoiner("\n").add("");
        int counter = 0;
        for(User friend : friends){
            stringJoiner.add("#" + ++counter + " " + friend.getName());
        }
        stringJoiner.add("Всего: " + counter + ".")
                .add("Введите номер друга: ");
        System.out.print(stringJoiner);

        friend =  friends.get(scanner.nextInt() - 1);
        scanner.nextLine();
    }

    private void sendMessage(){
        Message message = new Message();
        message.setSender(currentUser);
        message.setRecipient(friend);

        System.out.print("Введите сообщение: ");
        message.setText(scanner.nextLine());

        DataBaseServices.getInstance().messageGenericDAO.update(message);
    }
}
