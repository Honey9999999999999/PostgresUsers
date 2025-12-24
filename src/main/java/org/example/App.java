package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.model.*;
import org.example.pages.Navigator;

@Slf4j
public class App 
{
    private static final Navigator navigator = Navigator.getInstance();

    public static void main(String[] args){
        while (navigator.isRunning()) {
            navigator.update();
        }

        log.info("Программа завершена.");
    }
}
