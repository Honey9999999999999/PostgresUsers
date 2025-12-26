package org.example.util;

import org.example.dao.*;
import org.example.model.Content;
import org.example.model.Message;
import org.example.model.User;
import org.example.service.ContentService;
import org.example.service.UserService;

public class DataBaseServices {
    private static DataBaseServices instance;

    public UserService userService;
    public ContentService contentService;

    public final FriendShipDAO friendShipDAO;
    public final GenericDAO<Message> messageGenericDAO;
    public final MessageDAO messageDAO;
    public final PasswordDAO passwordDAO;

    private DataBaseServices(){
        userService = new UserService(new GenericDAO<>(User.class));
        contentService = new ContentService(new GenericDAO<>(Content.class), new ContentDAO());

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
