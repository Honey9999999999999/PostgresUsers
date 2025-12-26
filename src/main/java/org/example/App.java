package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.page.Navigator;

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
