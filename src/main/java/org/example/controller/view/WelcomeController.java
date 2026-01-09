package org.example.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class WelcomeController {
    @GetMapping("/welcome")
    public String welcomePage(Model model){
        model.addAttribute("message", "Осталось разобраться че за Model");
        model.addAttribute("serverTime", LocalDateTime.now());
        return "welcome";
    }
}
