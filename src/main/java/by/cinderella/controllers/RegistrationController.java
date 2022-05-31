package by.cinderella.controllers;

import by.cinderella.model.user.Role;
import by.cinderella.model.user.User;
import by.cinderella.repos.UserRepo;
import by.cinderella.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;


    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user,
                          Map<String, Object> model,
                          @RequestParam("confirmPassword") String confirmPassword) {


        if (!confirmPassword.equals(user.getPassword())) {
            model.put("message", "Пароли не совпадают!");
            return "registration";
        }

        String result = userService.addUser(user);
        if (result != null) {
            model.put("message", result);
            return "registration";
        } else {
            model.put("message", "На вашу почту отправлено письмо. Следуйте инструкциям для активации!");
            return "login";
        }
    }

    @GetMapping("/activate/{code}")
    public String activateUser(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            model.addAttribute("message", "Поздравляем! Аккаунт успешно активирован.");
        } else {
            model.addAttribute("message", "Код активации не найден! Попробуйте скопировать ссылку!");
        }

        return "login";
    }
}
