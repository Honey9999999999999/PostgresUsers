package org.example.util;

import org.example.dao.*;
import org.example.model.Message;
import org.example.model.User;

public class DataBaseServices {
    private static DataBaseServices instance;

    public final UserDAO userDAO;
    public final ContentDAO contentDAO;
    public final FriendShipDAO friendShipDAO;
    public final GenericDAO<Message> messageGenericDAO;
    public final MessageDAO messageDAO;
    public final PasswordDAO passwordDAO;

    private DataBaseServices(){
        userDAO = new UserDAO();
        contentDAO = new ContentDAO();
        friendShipDAO = new FriendShipDAO();
        messageDAO = new MessageDAO();
        messageGenericDAO = new GenericDAO<>(Message.class);
        passwordDAO = new PasswordDAO();
    }

    public static DataBaseServices getInstance(){
        if(instance == null){
            instance = new DataBaseServices();
        }

        return instance;
    }
}
