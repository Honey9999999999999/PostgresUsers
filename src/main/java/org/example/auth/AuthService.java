package org.example.auth;

import org.example.dao.UserDAO;

public class AuthService {

    private static AuthService instance;

    private final UserDAO userDAO;
    private Long currentUserId = 0L;

    private AuthService(){
        userDAO = new UserDAO();
    }

    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public boolean login(Long userId) {
        if(userDAO.findById(userId) != null){
            currentUserId = userId;
            return true;
        }

        return false;
    }

    public void logout() {
        this.currentUserId = 0L;
    }

    public boolean isAuth(){
        return currentUserId > 0;
    }
}
