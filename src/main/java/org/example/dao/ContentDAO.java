package org.example.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Content;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

@Slf4j
public class ContentDAO {
    public <T extends Content> List<T> findContentByUserId(Class<T> type, Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM " + type.getSimpleName() + " c WHERE c.user.id = :userId ORDER BY c.createdAt DESC";

            return session.createQuery(hql, type)
                    .setParameter("userId", userId)
                    .list();
        }
    }
}
