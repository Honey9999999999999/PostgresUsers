package org.example.dao;

import org.example.model.Comment;
import org.example.model.Content;
import org.example.model.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ContentDAO {
    public Content findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Content.class, id);
        }
    }

    public List<Content> findContentByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Content c WHERE c.user.id = :userId ORDER BY c.createdAt DESC";

            List<Content> results = session.createQuery(hql, Content.class)
                    .setParameter("userId", userId)
                    .list();

            return results;
        }
    }

    public void addComment(Long userId, Long contentId, Comment comment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            comment.addUser(session.get(User.class, userId));
            Content content = session.get(Content.class, contentId);
            content.addComment(comment);

            transaction.commit();
        }
    }
}
