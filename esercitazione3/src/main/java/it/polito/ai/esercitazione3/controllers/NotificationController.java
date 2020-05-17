package it.polito.ai.esercitazione3.controllers;

import it.polito.ai.esercitazione3.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;


@Controller
@RequestMapping("/API/notification")
public class NotificationController {

    String home = "notification.html";

    @Autowired
    NotificationService notificationService;

    @GetMapping("/confirm/{token}")
    public String confirm(@PathVariable String token, Model model){
        if (notificationService.confirm(token))
            model.addAttribute("message", "Your subscription is successful confirmed!");
        else
            model.addAttribute("message", "Something go wrong!\n\nToken: " + token);
        return home;
    }

    @GetMapping("/reject/{token}")
    public String reject(@PathVariable String token, Model model){
        if (notificationService.reject(token))
            model.addAttribute("message", "Your subscription is successful rejected!");
        else
            model.addAttribute("message", "Something go wrong!\n\nToken: " + token);
        return home;
    }
}

