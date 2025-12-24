package org.example.dao;

import org.example.model.Password;
import org.example.util.HibernateUtil;
import org.example.util.PasswordHasher;
import org.hibernate.Session;

public class PasswordDAO {
    public boolean isCorrectPassword(Long userId, String password){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Password passwordBD = session.get(Password.class, userId);
            return PasswordHasher.hashPassword(password, passwordBD.getSalt()).equals(passwordBD.getPassword());
        }
    }
}
