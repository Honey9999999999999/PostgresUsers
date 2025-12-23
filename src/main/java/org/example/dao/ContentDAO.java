package org.example.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Article;
import org.example.model.Comment;
import org.example.model.Content;
import org.example.model.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Slf4j
public class ContentDAO {
    public Content findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Content.class, id);
        }
    }

    public List<Content> findContentByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Content c WHERE c.user.id = :userId ORDER BY c.createdAt DESC";

            return session.createQuery(hql, Content.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    public List<Article> findArticleByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Article c WHERE c.user.id = :userId ORDER BY c.createdAt DESC";

            return session.createQuery(hql, Article.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    public List<Comment> findCommentByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Comment c WHERE c.user.id = :userId ORDER BY c.createdAt DESC";

            return session.createQuery(hql, Comment.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    public void createComment(Long userId, Long contentId, Comment comment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            comment.addUser(session.get(User.class, userId));
            Content content = session.get(Content.class, contentId);
            content.addComment(comment);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            log.error("Ошибка при выполнении транзакции в ContentDAO.createComment", e);
        }
    }

    public void createArticle(Long id, Content post) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                user.addPost(post);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            log.error("Ошибка при выполнении транзакции в ContentDAO.createArticle", e);
        }
    }
}
