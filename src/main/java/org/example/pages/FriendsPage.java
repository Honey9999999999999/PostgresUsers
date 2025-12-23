package org.example.pages;

import lombok.extern.slf4j.Slf4j;
import org.example.auth.AuthService;
import org.example.dao.FriendShipDAO;
import org.example.dao.UserDAO;
import org.example.model.Friendship;
import org.example.model.User;
import org.example.util.DataBaseServices;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringJoiner;

@Slf4j
public class FriendsPage extends BranchPage{
    private final UserDAO userDAO;
    private final FriendShipDAO friendShipDAO;

    private User currentUser;
    private List<Friendship> friends;

    public FriendsPage(Navigator navigator) {
        super(navigator);

        userDAO = DataBaseServices.getInstance().userDAO;
        friendShipDAO = DataBaseServices.getInstance().friendShipDAO;
    }

    @Override
    public String getName() {
        return "Друзья";
    }

    @Override
    protected LinkedHashMap<Integer, MenuItem> createMenu() {
        LinkedHashMap<Integer, MenuItem> menuMap = new LinkedHashMap<>();

        menuMap.put(1, new MenuItem("Отправить запрос в друзья", this::sendRequest));
        menuMap.put(2, new MenuItem("Просмотреть исходящие запросы", this::getOutRequests));
        menuMap.put(3, new MenuItem("Просмотреть входящие запросы", this::getInRequests));

        return menuMap;
    }

    @Override
    public void onEnter(){
        super.onEnter();
        currentUser = userDAO.findById(AuthService.getInstance().getCurrentUserId());
        friends = friendShipDAO.getFriends(currentUser.getId());
    }

    @Override
    public void onExit(){
        super.onExit();
        currentUser = null;
    }

    @Override
    protected String getBody(){
        StringJoiner stringJoiner = new StringJoiner("\n");
        int counter = 0;
        for(Friendship friendship : friends){
            User user = currentUser.getId().equals(friendship.getId().getUserId())
                    ? userDAO.findById(friendship.getId().getFriendId())
                    : userDAO.findById(friendship.getId().getUserId());
            stringJoiner.add(user.getName());
            counter++;
        }
        stringJoiner.add("Всего: " + counter + ".");

        return stringJoiner.toString();
    }

    private void sendRequest(){
        System.out.print("Введите ID пользователя: ");
        User user = userDAO.findById(scanner.nextLong());
        scanner.nextLine();

        if(user == null){
            log.info("Пользователь не найден.");
            return;
        }

        friendShipDAO.createFriendShip(currentUser, user);
    }
    private void getOutRequests(){
        List<Friendship> requests = friendShipDAO.getOutFriendShip(currentUser.getId());
        StringBuilder stringBuilder = new StringBuilder("-Исходящие запросы-\n");

        for(Friendship friendship : requests){
            User friend = userDAO.findById(friendship.getId().getFriendId());
            stringBuilder.append("\t* Запрос к ")
                    .append(friend.getName())
                    .append(" | status : ")
                    .append(friendship.getStatus())
                    .append(" | от ")
                    .append(friendship.getCreatedAt())
                    .append("\n");
        }

        if(!stringBuilder.isEmpty())
            messages.add(stringBuilder.toString());
    }
    private void getInRequests(){
        navigator.enterIn(InRequestsPage.class);
    }
}
