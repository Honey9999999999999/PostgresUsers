package org.example.page;

import org.example.auth.AuthService;
import org.example.model.Message;
import org.example.model.User;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class ChatPage extends BranchPage{
    private User currentUser;
    private User friend;

    private Map<Long, User> userMap;

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
//        menuMap.put(1, new MenuItem("Отправить сообщение", this::sendMessage));

        return menuMap;
    }

    @Override
    public void onEnter(){
        currentUser = AuthService.getInstance().getCurrentUser();
//        friend = chooseFriend();

        if(friend == null){
            navigator.enterIn(UserPage.class);
            return;
        }

        userMap = Map.of(
          currentUser.getId(), currentUser,
          friend.getId(), friend
        );
    }

    @Override
    protected String getBody(){
//        List<Message> messages = DataBaseServices.getInstance().messageDAO.getMessages(currentUser.getId(), friend.getId());
        StringJoiner stringJoiner = new StringJoiner("\n");

//        for(Message message : messages){
//            stringJoiner.add(userMap.get(message.getId().getSenderId()).getName() + " : " + message.getText());
//        }

        return stringJoiner.toString();
    }

//    private User chooseFriend(){
//        List<User> friends = DataBaseServices.getInstance().friendShipDAO.getFriends(currentUser.getId());
//
//        if(friends.isEmpty()){
//            System.out.print("У вас нет друзей :с");
//            return null;
//        }
//
//        StringJoiner stringJoiner = new StringJoiner("\n").add("");
//        int counter = 0;
//        for(User friend : friends){
//            stringJoiner.add("#" + ++counter + " " + friend.getName());
//        }
//
//        stringJoiner.add("Всего: " + counter + ".").add("Введите номер друга: ");
//        System.out.print(stringJoiner);
//
//        return friends.get(safeScanner.nextNumber(Integer.class, 1, friends.size()) - 1);
//    }
//
//    private void sendMessage(){
//        Message message = new Message(currentUser, friend);
//
//        System.out.print("Введите сообщение: ");
//        message.setText(scanner.nextLine());
//
//        DataBaseServices.getInstance().messageGenericDAO.update(message);
//    }
}
