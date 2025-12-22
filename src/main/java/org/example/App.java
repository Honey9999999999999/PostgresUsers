package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.auth.AuthService;
import org.example.model.*;
import org.example.pages.Navigator;

@Slf4j
public class App 
{
    private static Navigator navigator;

    static {
        initialize();
    }

    private static void initialize(){
        AuthService.getInstance();
        navigator = Navigator.getInstance();
    }

    public static void main( String[] args )
    {
        initialize();

        while (navigator.isRunning()) {
            navigator.update();
        }

        log.info("Программа завершена.");
    }
}
