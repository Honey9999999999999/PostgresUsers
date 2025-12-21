package org.example.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

@Slf4j
public class UserDAO {

    // CREATE
    public void save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            log.error("Ошибка при выполнении транзакции в UserDAO.save", e);
        }
    }

    // READ (один)
    public User findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }

    // READ (все)
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    // UPDATE
    public void update(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            log.error("Ошибка при выполнении транзакции в UserDAO.update", e);
        }
    }

    // DELETE
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            int updatedEntities = session.createMutationQuery("delete from User u where u.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            transaction.commit();

            if (updatedEntities == 0) {
                log.info("Пользователь с таким ID не найден, ничего не удалено.");
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            log.error("Ошибка при выполнении транзакции в UserDAO.delete", e);
        }
    }
}