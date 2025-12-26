package org.example.auth;

import lombok.Getter;
import org.example.model.User;
import org.example.util.DataBaseServices;

@Getter
public class AuthService {

    private static AuthService instance;
    private User currentUser;

    private AuthService(){}

    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public boolean login(Long userId, String password) {
        User tempUser = DataBaseServices.getInstance().userService.findById(userId);

        if(tempUser != null
                && DataBaseServices.getInstance().passwordDAO.isCorrectPassword(userId, password)){
            currentUser = tempUser;
            return true;
        }

        return false;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isAuth(){
        return currentUser != null;
    }
}
