package org.example;

import org.example.dao.ContentDAO;
import org.example.dao.RequestDAO;
import org.example.dao.UserDAO;
import org.example.model.*;

import java.util.*;

public class App 
{
    private static UserDAO userDAO;
    private static ContentDAO contentDAO;
    private static RequestDAO requestDAO;
    private static Scanner scanner;

    private static final Map<PageType, String> headerMap = Map.of(
            PageType.Main, "---Главное меню---",
            PageType.User, "---Меню пользователя---"
    );

    private static final Map<PageType, Map<Integer, MenuItem>> menusMap = Map.of(
            PageType.Main, new LinkedHashMap<>(),
            PageType.User, new LinkedHashMap<>()
    );

    static {
        Map<Integer, MenuItem> menu = menusMap.get(PageType.Main);

        menu.put(1, new MenuItem("Войти в профиль", App::singIn));
        menu.put(2, new MenuItem("Создать пользователя", App::createUser));
        menu.put(3, new MenuItem("Найти пользователя по ID", App::findUserById));
        menu.put(4, new MenuItem("Показать всех пользователей", App::showAllUsers));
        menu.put(5, new MenuItem("Обновить пользователя", App::updateUser));
        menu.put(6, new MenuItem("Удалить пользователя", App::deleteUser));
        menu.put(0, new MenuItem("Выход", App::closeApp));

        menu = menusMap.get(PageType.User);

        menu.put(1, new MenuItem("Написать пост", App::createPost));
        menu.put(2, new MenuItem("Показать все посты", App::getMyPosts));
        menu.put(3, new MenuItem("Показать посты другого пользователя", App::getAnotherPosts));
        menu.put(4, new MenuItem("Прокомментировать пост", App::createComment));
        menu.put(5, new MenuItem("Отправить запрос в друзья", App::sendRequest));
        menu.put(6, new MenuItem("Просмотреть отправленные запросы", App::getOutRequests));
        menu.put(7, new MenuItem("Просмотреть входящие запросы", App::getInRequests));
        menu.put(0, new MenuItem("Выйти из профиля", App::singOut));
    }

    private static PageType currentPage = PageType.Main;
    private static User currentUser;

    private static Boolean isRunning = true;

    public static void main( String[] args )
    {
        initialize();

        while (isRunning) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            menusMap.get(currentPage).get(choice).getAction().run();
        }

        System.out.println("Программа завершена.");
    }

    private static void initialize() {
        userDAO = new UserDAO();
        contentDAO = new ContentDAO();
        requestDAO = new RequestDAO();
        scanner = new Scanner(System.in);
    }

    private static void printMenu(){
        StringBuilder stringBuilder = new StringBuilder("\n").append(headerMap.get(currentPage));
        Set<Map.Entry<Integer, MenuItem>> menuItems = menusMap.get(currentPage).entrySet();

        for(var item : menuItems){
            stringBuilder.append("\n").append(item.getKey()).append(": ").append(item.getValue().getTitle());
        }
        stringBuilder.append("\nВыберите действие: ");

        System.out.print(stringBuilder);
    }

    private static void singIn(){
        currentUser = findUserById();
        currentPage = currentUser != null ? PageType.User : PageType.Main;
    }
    private static void createUser(){
        User user = new User();
        System.out.print("Введите имя: ");
        user.setName(scanner.nextLine());
        System.out.print("Введите email: ");
        user.setEmail(scanner.nextLine());
        System.out.print("Введите возраст: ");
        user.setAge(scanner.nextInt());
        userDAO.save(user);
        System.out.println("Пользователь сохранен!");
    }
    private static User findUserById(){
        System.out.print("Введите ID пользователя: ");
        Long id = scanner.nextLong();
        User user = userDAO.findById(id);
        System.out.println(user != null ? "Найдено: " + user.getName() : "Не найден");

        return user;
    }
    private static void showAllUsers(){
        userDAO.findAll().forEach(u ->
                System.out.println("ID#" + u.getId() + ": " + u.getName() + " (" + u.getEmail() + ")"));
    }
    private static void updateUser(){
        System.out.print("Введите ID пользователя: ");
        User user = userDAO.findById(scanner.nextLong());
        scanner.nextLine();

        System.out.print("Введите новое имя: ");
        user.setName(scanner.nextLine());
        System.out.print("Введите новый email: ");
        user.setEmail(scanner.nextLine());
        System.out.print("Введите новый возраст: ");
        user.setAge(scanner.nextInt());
        userDAO.update(user);
        System.out.println("Пользователь обновлен!");
    }
    private static void deleteUser(){
        System.out.print("Введите ID для удаления: ");
        userDAO.delete(scanner.nextLong());
        System.out.println("Готово.");
    }
    private static void closeApp(){
        isRunning = false;
    }

    private static void createPost(){
        Article post = new Article();
        System.out.print("Введите название поста: ");
        post.setTitle(scanner.nextLine());
        System.out.print("Введите содержание: ");
        post.setBody(scanner.nextLine());

        contentDAO.createArticle(currentUser.getId(), post);
    }
    private static void getPosts(long id){
        contentDAO.findContentByUserId(id).forEach(p ->
            System.out.println("ID#" + p.getId() + " : " + p.getTitle() + " : " + p.getCreatedAt()));
    }
    private static void getMyPosts(){
        getPosts(currentUser.getId());
    }
    private static void getAnotherPosts(){
        System.out.print("Введите id пользователя: ");
        getPosts(scanner.nextLong());
    }
    private static void createComment(){
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

        contentDAO.addComment(currentUser.getId(), content.getId(), comment);
    }
    private static void sendRequest(){
        User user = findUserById();
        Request request = new Request();
        request.setSender(currentUser);
        request.setRecipient(user);

        requestDAO.createRequest(request);
    }
    private static void getOutRequests(){
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
    private static void getInRequests(){
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
    private static void singOut(){
        currentUser = null;
        currentPage = PageType.Main;
    }
}
