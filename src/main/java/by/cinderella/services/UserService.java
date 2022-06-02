package by.cinderella.services;

import by.cinderella.model.user.Role;
import by.cinderella.model.user.User;
import by.cinderella.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${application.url}")
    private String applicationUrl;

    @Autowired
    CinderellaMailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return (UserDetails) userRepo.findByUsername(userName);
    }

    public String addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return "Пользователь с таким именем существует!";
        }

        user.setActive(false);

        user.setRoles(Collections.singleton(Role.USER));
        user.setRegistrationDate(new Date());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString());

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Приветствую, %s! \n" +
                            "Добро пожаловать на cinderella.by! \n" +
                            "Для активации аккаунта, пожалуйста, пройди по ссылке: %s/activate/%s\n" +
                            "Если вы не регистрировались на нашем сайте, то просто проигнорируйте данное письмо! \n" +
                            "\n" +
                            "Хорошего дня!",
                    user.getUsername(),
                    applicationUrl,
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Активация аккаунта cinderella.by", message);
        } else {
            return "Укажите валидный Email-адрес!";
        }



        userRepo.save(user);

        return null;
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationDate(new Date());
        user.setActivationCode(null);
        user.setActive(true);

        userRepo.save(user);
        return true;
    }
}
