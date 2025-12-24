package org.example.pages;

import org.example.auth.AuthService;
import org.example.dao.ContentDAO;
import org.example.model.Article;
import org.example.model.Comment;
import org.example.model.Content;
import org.example.model.User;
import org.example.util.DataBaseServices;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class NewsPage extends BranchPage{
    private final ContentDAO contentDAO;
    private User currentUser;

    private final List<Article> posts = new ArrayList<>();

    public NewsPage(Navigator navigator) {
        super(navigator);

        contentDAO = DataBaseServices.getInstance().contentDAO;
    }

    @Override
    public String getName() {
        return "Новости";
    }

    @Override
    protected LinkedHashMap<Integer, MenuItem> createMenu() {
        LinkedHashMap<Integer, MenuItem> menuMap = new LinkedHashMap<>();

        menuMap.put(1, new MenuItem("Написать пост", this::createPost));
        menuMap.put(2, new MenuItem("Показать все посты", this::getMyPosts));
        menuMap.put(3, new MenuItem("Показать посты другого пользователя", this::getAnotherPosts));
        menuMap.put(4, new MenuItem("Прокомментировать пост", this::createComment));

        return menuMap;
    }

    @Override
    public void onEnter(){
        super.onEnter();
        currentUser = AuthService.getInstance().getCurrentUser();
        posts.addAll(contentDAO.findArticleByUserId(currentUser.getId()));
    }
    @Override
    public void onExit(){
        super.onExit();
        posts.clear();
        currentUser = null;
    }

    @Override
    protected String getBody(){
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < posts.size(); i++){
            stringBuilder.append("#")
                    .append(i + 1)
                    .append(": ")
                    .append(posts.get(i).getTitle())
                    .append(" : ")
                    .append(posts.get(i).getBody())
                    .append(" : ")
                    .append(posts.get(i).getCreatedAt())
                    .append("\n");
        }

        return new String(stringBuilder);
    }

    private void createPost(){
        Article post = new Article();
        System.out.print("Введите название поста: ");
        post.setTitle(scanner.nextLine());
        System.out.print("Введите содержание: ");
        post.setBody(scanner.nextLine());

        contentDAO.createArticle(currentUser.getId(), post);
    }
    private void getPosts(long id){
        contentDAO.findContentByUserId(id).forEach(p ->
                System.out.println("ID#" + p.getId() + " : " + p.getTitle() + " : " + p.getCreatedAt()));
    }
    private void getMyPosts(){
        getPosts(currentUser.getId());
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
}
