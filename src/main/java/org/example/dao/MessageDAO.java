package org.example.dao;

import org.example.model.Message;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class MessageDAO {
    public List<Message> getMessages(Long senderId, Long recipientId){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            String hql = "FROM Message m WHERE m.sender.id = :senderId AND m.recipient.id = :recipientId";
            return session.createQuery(hql, Message.class)
                    .setParameter("senderId", senderId)
                    .setParameter("recipientId", recipientId)
                    .list();
        }
    }
}
