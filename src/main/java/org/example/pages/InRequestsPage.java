package org.example.pages;

import org.example.auth.AuthService;
import org.example.dao.FriendShipDAO;
import org.example.model.Friendship;
import org.example.model.User;
import org.example.util.DataBaseServices;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class InRequestsPage extends BranchPage{
    private final List<Friendship> requests = new ArrayList<>();
    private final FriendShipDAO friendShipDAO;

    private User currentUser;

    public InRequestsPage(Navigator navigator) {
        super(navigator);
        friendShipDAO = DataBaseServices.getInstance().friendShipDAO;
    }

    @Override
    public String getName() {
        return "Входящие запросы";
    }

    @Override
    protected LinkedHashMap<Integer, MenuItem> createMenu() {
        LinkedHashMap<Integer, MenuItem> menuMap = new LinkedHashMap<>();

        menuMap.put(1, new MenuItem("Принять запрос в друзья", this::acceptRequest));
        menuMap.put(2, new MenuItem("Отказаться от запроса в друзья", this::refuseRequest));

        return menuMap;
    }

    @Override
    public void onEnter(){
        super.onEnter();
        currentUser = DataBaseServices.getInstance().userDAO.findById(AuthService.getInstance().getCurrentUserId());
        requests.addAll(friendShipDAO.getInFriendShip(currentUser.getId()));
    }
    @Override
    public void onExit(){
        super.onExit();
        requests.clear();
    }

    @Override
    public String getBody(){
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < requests.size(); i++){
            User user = DataBaseServices.getInstance().userDAO.findById(requests.get(i).getId().getUserId());
            stringBuilder.append("#").append(i + 1).append(" Запрос от ")
                    .append(user.getName())
                    .append(" | status : ")
                    .append(requests.get(i).getStatus())
                    .append(" | от ")
                    .append(requests.get(i).getCreatedAt())
                    .append("\n");
        }

        return stringBuilder.toString();
    }

    private void acceptRequest(){

    }
    private void refuseRequest(){

    }
}
