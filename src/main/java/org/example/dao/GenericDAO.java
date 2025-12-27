package org.example.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Slf4j
public record GenericDAO<T>(Class<T> entityClass) {
    // CREATE
    public <C extends T> void save(C obj) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(obj);
            transaction.commit();
        } catch (Exception e) {
            log.error("Ошибка при выполнении транзакции в GenericDAO.save", e);
            throw e;
        }
    }

    // READ (one)
    public T findById(Long id) {
        return findById(entityClass, id);
    }
    public <C extends T> C findById(Class<C> type, Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(type, id);
        }
    }

    // READ (all)
    public List<T> findAll() {
        return findAll(entityClass);
    }
    public <C extends T> List<C> findAll(Class<C> type) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM " + type.getName(), type).list();
        }
    }

    // UPDATE
    public <C extends T> void update(C obj) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(obj);
            transaction.commit();
        } catch (Exception e) {
            log.error("Ошибка при выполнении транзакции в GenericDAO.update", e);
            throw e;
        }
    }

    // DELETE
    public void delete(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            int updatedEntities = session.createMutationQuery("DELETE FROM " + entityClass.getName() + " WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            transaction.commit();

            if (updatedEntities == 0) {
                log.info("Объект с таким ID не найден, ничего не удалено.");
            }
        } catch (Exception e) {
            log.error("Ошибка при выполнении транзакции в GenericDAO.delete", e);
            throw e;
        }
    }
    public void delete(T obj) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(obj);
            transaction.commit();
        } catch (Exception e) {
            log.error("Ошибка при выполнении транзакции в GenericDAO.delete", e);
            throw e;
        }
    }
}
