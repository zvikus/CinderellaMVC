package by.cinderella.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {
    @GetMapping("")
    public String mainPAge(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Главная");

        return  "index";
    }
}
