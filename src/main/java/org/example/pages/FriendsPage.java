package org.example.pages;

import org.example.auth.AuthService;
import org.example.dao.RequestDAO;
import org.example.dao.UserDAO;
import org.example.model.Request;
import org.example.model.User;
import org.example.util.DataBaseServices;

import java.util.LinkedHashMap;
import java.util.List;

public class FriendsPage extends BranchPage{
    private final UserDAO userDAO;
    private final RequestDAO requestDAO;

    private User currentUser;

    public FriendsPage(Navigator navigator) {
        super(navigator);

        userDAO = DataBaseServices.getInstance().userDAO;
        requestDAO = DataBaseServices.getInstance().requestDAO;
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
    }

    @Override
    public void onExit(){
        super.onExit();
        currentUser = null;
    }

    private void sendRequest(){
        System.out.print("Введите ID пользователя: ");
        User user = userDAO.findById(scanner.nextLong());
        scanner.nextLine();

        Request request = new Request();
        request.setSender(currentUser);
        request.setRecipient(user);

        requestDAO.createRequest(request);
    }
    private void getOutRequests(){
        List<Request> requests = requestDAO.getOutRequests(currentUser.getId());
        StringBuilder stringBuilder = new StringBuilder("-Исходящие запросы-\n");

        for(Request request : requests){
            stringBuilder.append("\t* Запрос к ")
                    .append(request.getRecipient().getName())
                    .append(" от ")
                    .append(request.getCreatedAt())
                    .append("\n");
        }

        if(!stringBuilder.isEmpty())
            messages.add(stringBuilder.toString());
    }
    private void getInRequests(){
        navigator.enterIn(InRequestsPage.class);
    }
}
