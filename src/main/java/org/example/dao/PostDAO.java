package org.example.dao;

import org.example.model.Post;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class PostDAO {
    public List<Post> getPostsByID(Long userId){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            return session.createQuery("from Post p where p.user.id = :userId", Post.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }
}
