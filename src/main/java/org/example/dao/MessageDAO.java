package org.example.dao;

import org.example.model.Message;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class MessageDAO {
    public List<Message> getMessages(Long senderId, Long recipientId){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            String hql = "FROM Message m " +
                    "WHERE (m.sender.id = :senderId AND m.recipient.id = :recipientId) " +
                    "OR (m.sender.id = :recipientId AND m.recipient.id = :senderId) " +
                    "ORDER BY m.id.createdAt DESC";
            return session.createQuery(hql, Message.class)
                    .setParameter("senderId", senderId)
                    .setParameter("recipientId", recipientId)
                    .setMaxResults(10)
                    .list().reversed();
        }
    }
}
