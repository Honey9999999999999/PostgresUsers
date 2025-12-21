package org.example.dao;


import lombok.extern.slf4j.Slf4j;
import org.example.model.Request;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Slf4j
public class RequestDAO {
    public void createRequest(Request request){
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.persist(request);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            log.error("Ошибка при выполнении транзакции в RequestDAO.createRequest", e);
        }
    }

    public List<Request> getSendRequests(Long senderId){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            String hql = "FROM Request r WHERE r.sender.id = :userId";

            return session.createQuery(hql, Request.class).setParameter("userId", senderId).list();
        }
    }

    public List<Request> getInRequests(Long recipientId){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            String hql = "FROM Request r WHERE r.recipient.id = :userId";

            return session.createQuery(hql, Request.class).setParameter("userId", recipientId).list();
        }
    }
}
