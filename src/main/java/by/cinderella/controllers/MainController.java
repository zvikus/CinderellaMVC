package by.cinderella.controllers;

import by.cinderella.model.user.Restriction;
import by.cinderella.model.user.Service;
import by.cinderella.model.user.User;
import by.cinderella.repos.RestrictionRepo;
import by.cinderella.repos.ServiceRepo;
import by.cinderella.repos.UserRepo;
import by.cinderella.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Controller
public class MainController {
    @GetMapping("")
    public String mainPage(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Главная");

        return  "index";
    }

    @GetMapping("/about")
    public String aboutPage(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Обо мне");

        return  "main/about";
    }

    @GetMapping("/contacts")
    public String contactsPage(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Контакты");

        return  "main/contacts";
    }

    @Value("{yoomoney.check}")
    private String yoomoneyCheck;

    @Autowired
    ServiceRepo serviceRepo;

    @Autowired
    RestrictionRepo restrictionRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/onlineServices")
    public String userServices(HttpServletRequest request, Model model) {
        model.addAttribute("services", serviceRepo.findByPublishedTrue());
        return "user/services";
    }

    @PostMapping("/yoomoneyPayment")
    public ResponseEntity yoomoneyPayment(@RequestParam("operation_id") String operationId,
                                          @RequestParam("label") String details,
                                          @RequestParam("sha1_hash") String hash) {
        if (yoomoneyCheck.equals(hash)) {
            return ResponseEntity.badRequest().build();
        }
        String[] params = details.split(",");
        Long serviceId = new Long(params[0]);
        Long userId = new Long(params[1]);

        Optional<Service> service = serviceRepo.findById(serviceId);
        Optional<User> user = userRepo.findById(userId);
        if (user.isPresent() && service.isPresent()) {
            Restriction restriction = new Restriction();
            restriction.setActivationDate(new Date());

            if (service.get().isSubscription()) {
                Integer term = new Integer(params[2]);

                Date serviceExpirationDate = userService.getServiceExpirationDate(serviceId, userId);
                if (serviceExpirationDate == null) {
                    serviceExpirationDate = new Date();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(serviceExpirationDate);
                cal.add(Calendar.MONTH, term);
                restriction.setExpirationDate(cal.getTime());
            }
            restriction.setService(service.get());
            restriction.setUser(user.get());
            user.get().getRestrictions().add(restriction);
            userRepo.save(user.get());
            restrictionRepo.save(restriction);
        } else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
