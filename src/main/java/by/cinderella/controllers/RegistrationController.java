package by.cinderella.controllers;

import by.cinderella.model.user.Role;
import by.cinderella.model.user.User;
import by.cinderella.repos.UserRepo;
import by.cinderella.services.CinderellaMailSender;
import by.cinderella.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.UUID;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @Autowired
    private CinderellaMailSender mailSender;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${application.url}")
    private String applicationUrl;

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

    @GetMapping("passReminder")
    public String passReminderForm() {
        return "passReminder";
    }

    @PostMapping("passReminder")
    public String passReminder(Map<String, Object> model,
            @RequestParam("email") String email) {
        User userFromDb = userRepo.findByEmailAndActiveTrue(email);
        if (userFromDb == null) {
            model.put("errorMessage", "Пользователь с таким адресом не найден!");
            return "passReminder";
        } else {
            model.put("message", "Спасибо! Вам на почту были отправлены дальнейшие инструкции!");
        }

        userFromDb.setActivationCode(UUID.randomUUID().toString());
        userRepo.save(userFromDb);

        String message = String.format(
                "Приветствую, %s! \n" +
                        "Для восстановления пароля, пожалуйста, пройди по ссылке: %s/passReminder/%s\n" +
                        "Ваш логин: %s\n" +
                        "Если это письмо пришло к Вам по ошибке, то просто проигнорируйте его! \n" +
                        "\n" +
                        "Хорошего дня!",
                userFromDb.getUsername(),
                applicationUrl,
                userFromDb.getActivationCode(),
                userFromDb.getUsername()
        );
        mailSender.send(userFromDb.getEmail(), "Восстановление пароля cinderella.by", message);

        return "passReminder";
    }

    @GetMapping("/passReminder/{code}")
    public String passReminderUser(Model model, @PathVariable String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            model.addAttribute("message", "Пароль был восстановлен. Хотите повторить процедуру?");
            return "passReminder";
        }

        model.addAttribute("userId", user.getId());
        model.addAttribute("activationCode", user.getActivationCode());

        return "restorePassword";
    }

    @PostMapping("/restoreUserPassword")
    public String restoreUserPassword(Model model,
                                      @RequestParam("userId") Long userId,
                                      @RequestParam("activationCode") String activationCode,
                                      @RequestParam("password") String password,
                                      @RequestParam("confirmPassword") String confirmPassword) {
        User user = userRepo.getById(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("activationCode", activationCode);

        if (user == null) {
            model.addAttribute("errorMessage", "Ошибка! Повторно следуйте инструкциям в письме!");
            return "restorePassword";
        }

        if (!user.getActivationCode().equals(activationCode)) {
            model.addAttribute("errorMessage", "Ссылка устарела. Повторно пройдите процедуру восстановления пароля!");
            return "restorePassword";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Пароли не совпадают!");

            return "restorePassword";
        }

        user.setActivationCode(null);
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);
        model.addAttribute("message", "Поздравляем! Пароль восстановлен.");
        return "login";

    }
}
