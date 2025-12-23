package org.example.util;

import org.example.dao.ContentDAO;
import org.example.dao.RequestDAO;
import org.example.dao.UserDAO;

public class DataBaseServices {
    private static DataBaseServices instance;

    public final UserDAO userDAO;
    public final ContentDAO contentDAO;
    public final RequestDAO requestDAO;

    private DataBaseServices(){
        userDAO = new UserDAO();
        contentDAO = new ContentDAO();
        requestDAO = new RequestDAO();
    }

    public static DataBaseServices getInstance(){
        if(instance == null){
            instance = new DataBaseServices();
        }

        return instance;
    }
}
