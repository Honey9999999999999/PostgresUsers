package org.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (org.hibernate.service.spi.ServiceException e) {
            System.err.println("Критическая ошибка: Не удалось подключиться к PostgreSQL. Проверьте, запущен ли сервер БД.");
            throw e;
        } catch (Exception e) {
            System.err.println("Ошибка конфигурации Hibernate: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}