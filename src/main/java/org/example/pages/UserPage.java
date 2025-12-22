package org.example.pages;

import org.example.MenuItem;
import org.example.auth.AuthService;
import org.example.dao.ContentDAO;
import org.example.dao.RequestDAO;
import org.example.dao.UserDAO;
import org.example.model.*;

import java.util.LinkedHashMap;
import java.util.List;

public class UserPage extends Page{
    private ContentDAO contentDAO;
    private UserDAO userDAO;
    private RequestDAO requestDAO;
    private User currentUser;

    public UserPage(Navigator navigator) {
        super(navigator);

        contentDAO = new ContentDAO();
        userDAO = new UserDAO();
        requestDAO = new RequestDAO();
    }

    @Override
    public String getName() {
        return "Страница пользователя";
    }

    @Override
    protected LinkedHashMap<Integer, MenuItem> createMenu() {
        LinkedHashMap<Integer, MenuItem> menuMap = new LinkedHashMap<>();

        menuMap.put(1, new MenuItem("Написать пост", this::createPost));
        menuMap.put(2, new MenuItem("Показать все посты", this::getMyPosts));
        menuMap.put(3, new MenuItem("Показать посты другого пользователя", this::getAnotherPosts));
        menuMap.put(4, new MenuItem("Прокомментировать пост", this::createComment));
        menuMap.put(5, new MenuItem("Отправить запрос в друзья", this::sendRequest));
        menuMap.put(6, new MenuItem("Просмотреть отправленные запросы", this::getOutRequests));
        menuMap.put(7, new MenuItem("Просмотреть входящие запросы", this::getInRequests));
        menuMap.put(0, new MenuItem("Выйти из профиля", this::singOut));

        return menuMap;
    }

    @Override
    public void onEnter(){
        currentUser = userDAO.findById(AuthService.getInstance().getCurrentUserId());
    }

    @Override
    public void onExit(){
        currentUser = null;
    }

    private void createPost(){
        Article post = new Article();
        System.out.print("Введите название поста: ");
        post.setTitle(scanner.nextLine());
        System.out.print("Введите содержание: ");
        post.setBody(scanner.nextLine());

        contentDAO.createArticle(AuthService.getInstance().getCurrentUserId(), post);
    }
    private void getPosts(long id){
        contentDAO.findContentByUserId(id).forEach(p ->
                System.out.println("ID#" + p.getId() + " : " + p.getTitle() + " : " + p.getCreatedAt()));
    }
    private void getMyPosts(){
        getPosts(AuthService.getInstance().getCurrentUserId());
    }
    private void getAnotherPosts(){
        System.out.print("Введите id пользователя: ");
        getPosts(scanner.nextLong());
    }
    private void createComment(){
        System.out.print("Введите ID поста: ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        Content content = contentDAO.findById(id);
        System.out.println(content != null ? "Найдено: " + content.getTitle() : "Не найден");

        if(content == null) return;

        Comment comment = new Comment();
        comment.setTitle(currentUser.getName() + " комментирует " + content.getTitle());
        System.out.print("Введите содержание: ");
        comment.setText(scanner.nextLine());

        contentDAO.createComment(currentUser.getId(), content.getId(), comment);
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
        List<Request> requests = requestDAO.getSendRequests(currentUser.getId());
        StringBuilder stringBuilder = new StringBuilder();

        for(Request request : requests){
            stringBuilder.append("Запрос к ")
                    .append(request.getRecipient().getName())
                    .append(" от ")
                    .append(request.getCreatedAt());
        }

        System.out.println(stringBuilder);
    }
    private void getInRequests(){
        List<Request> requests = requestDAO.getInRequests(currentUser.getId());
        StringBuilder stringBuilder = new StringBuilder();

        for(Request request : requests){
            stringBuilder.append("Запрос от ")
                    .append(request.getSender().getName())
                    .append(" от ")
                    .append(request.getCreatedAt());
        }

        System.out.println(stringBuilder);
    }
    private void singOut(){
        navigator.enterIn(MainPage.class);
    }
}
