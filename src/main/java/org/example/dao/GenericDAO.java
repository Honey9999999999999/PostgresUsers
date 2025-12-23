package org.example.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Slf4j
public class GenericDAO<T> {
    private final Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // CREATE
    public void save(T obj) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(obj);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            log.error("Ошибка при выполнении транзакции в GenericDAO.save", e);
        }
    }

    // READ (один)
    public T findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(entityClass, id);
        }
    }

    // READ (все)
    public List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM " + entityClass.getName(), entityClass).list();
        }
    }

    // UPDATE
    public void update(T obj) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(obj);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            log.error("Ошибка при выполнении транзакции в GenericDAO.update", e);
        }
    }

    // DELETE
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            int updatedEntities = session.createMutationQuery("DELETE FROM \" + entityClass.getName() + \" WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            transaction.commit();

            if (updatedEntities == 0) {
                log.info("Объект с таким ID не найден, ничего не удалено.");
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            log.error("Ошибка при выполнении транзакции в GenericDAO.delete", e);
        }
    }
}
