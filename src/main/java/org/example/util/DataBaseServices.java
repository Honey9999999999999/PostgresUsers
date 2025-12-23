package org.example.util;

import org.example.dao.ContentDAO;
import org.example.dao.FriendShipDAO;
import org.example.dao.UserDAO;

public class DataBaseServices {
    private static DataBaseServices instance;

    public final UserDAO userDAO;
    public final ContentDAO contentDAO;
    public final FriendShipDAO friendShipDAO;

    private DataBaseServices(){
        userDAO = new UserDAO();
        contentDAO = new ContentDAO();
        friendShipDAO = new FriendShipDAO();
    }

    public static DataBaseServices getInstance(){
        if(instance == null){
            instance = new DataBaseServices();
        }

        return instance;
    }
}
