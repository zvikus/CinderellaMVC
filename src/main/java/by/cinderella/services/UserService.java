package by.cinderella.services;

import by.cinderella.model.user.Restriction;
import by.cinderella.model.user.Role;
import by.cinderella.model.user.User;
import by.cinderella.repos.RestrictionRepo;
import by.cinderella.repos.ServiceRepo;
import by.cinderella.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RestrictionRepo restrictionRepo;

    @Autowired
    private ServiceRepo serviceRepo;

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

    public boolean checkUserRestriction(Long serviceId) {

        User user = this.getAuthUser();
        if (user == null) {
            return false;
        }
        Optional<by.cinderella.model.user.Service> service = serviceRepo.findById(serviceId);
        if (!service.isPresent()) {
            return true;
        }
        List<Restriction> restrictions = restrictionRepo.findAllByUserAndService(user, Optional.of(service.get()));

        for (Restriction restriction : restrictions) {
            if (restriction.getExpirationDate().after(new Date())) {
                return  true;
            }
        }



        return false;
    }

    public User getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return userRepo.findByUsername(username);
    }
}
