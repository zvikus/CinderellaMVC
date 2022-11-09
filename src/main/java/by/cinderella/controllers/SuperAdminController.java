package by.cinderella.controllers;

import by.cinderella.config.Constants;
import by.cinderella.model.organizer.Organizer;
import by.cinderella.model.user.Restriction;
import by.cinderella.model.user.Role;
import by.cinderella.model.user.Service;
import by.cinderella.model.user.User;
import by.cinderella.repos.RestrictionRepo;
import by.cinderella.repos.ServiceRepo;
import by.cinderella.repos.UserRepo;
import by.cinderella.services.CinderellaMailSender;
import by.cinderella.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.expression.Calendars;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('SADMIN')")
public class SuperAdminController extends BaseController {

    @ModelAttribute("userRoles")
    public Set<Role> userRoles() {
        Set<Role> result  = new TreeSet<>();

        Collections.addAll(result, Role.values());
        return result;
    }
    @Autowired
    private CinderellaMailSender mailSender;

    @GetMapping("/users")
    public String getUsers(HttpServletRequest request, Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "sadmin/users";
    }

    @GetMapping("/user/{userId}/edit")
    public String editUser(@PathVariable(value = "userId") Long userId,
                                Model model) {
        if (!userRepo.existsById(userId)) {
            return "redirect:/admin/users";
        }

        Optional<User> user = userRepo.findById(userId);

        model.addAttribute("user", user.get());
        model.addAttribute("services", serviceRepo.findAll());
        model.addAttribute("title", "Администрирование - Редактировать " + user.get().getUsername());
        model.addAttribute("roles", new HashSet<>());

        return  "sadmin/editUser";
    }

    @PostMapping("/editUser")
    public String editUser(User user,
                                Model model) throws IOException {

        User parentUser = userRepo.getById(user.getId());

        user.setRegistrationDate(parentUser.getRegistrationDate());
        user.setPassword(parentUser.getPassword());
        user.setActivationDate(new Date());
        userRepo.save(user);
        return "redirect:/admin/users";
    }




    @GetMapping("/services")
    public String getServices(HttpServletRequest request, Model model) {
        model.addAttribute("services", serviceRepo.findAll());
        return "sadmin/services";
    }

    @GetMapping("/service/{serviceId}/edit")
    public String editService(@PathVariable(value = "serviceId") Long serviceId,
                                Model model) {
        if (!serviceRepo.existsById(serviceId)) {
            return "redirect:/admin/users";
        }

        Optional<Service> service = serviceRepo.findById(serviceId);

        model.addAttribute("service", service.get());
        model.addAttribute("title", "Администрирование - Редактировать " + service.get().getName());

        return  "sadmin/editService";
    }

    @GetMapping("/addService")
    public String addService(Model model) {

        model.addAttribute("service", new Service());
        model.addAttribute("title", "Администрирование - Новая услуга");

        return  "sadmin/editService";
    }

    @PostMapping("/editService")
    public String editService(Service service,
                                Model model) throws IOException {
        if (service.getId() != null) {
            Optional<Service> existsService = serviceRepo.findById(service.getId());
            existsService.ifPresent(value -> service.setRestrictions(value.getRestrictions()));
        }

        serviceRepo.save(service);

        return "redirect:/admin/services";
    }

    @GetMapping("/addRestriction")
    public String addRestriction(Model model) {

        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("services", serviceRepo.findAll());
        model.addAttribute("title", "Администрирование - Новая услуга");

        return  "sadmin/addRestriction";
    }

    @PostMapping("/addRestriction")
    public String addRestriction(@RequestParam("userId") Optional<Long> userId,
                                 @RequestParam("service") Optional<Long> serviceId,
                             @RequestParam("term") Optional<Integer> term,
                              Model model) {

        Optional<Service> service = serviceRepo.findById(serviceId.get());
        Optional<User> user = userRepo.findById(userId.get());


        Restriction restriction = new Restriction();

        restriction.setActivationDate(new Date());

        if (service.isPresent()) {
            if (service.get().isSubscription()) {
                Date serviceExpirationDate = userService.getServiceExpirationDate(serviceId.get(), user.get());
                if (serviceExpirationDate == null || serviceExpirationDate.before(new Date())) {
                    serviceExpirationDate = new Date();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(serviceExpirationDate);
                cal.add(Calendar.MONTH, term.get());
                restriction.setExpirationDate(cal.getTime());
            }
            restriction.setService(service.get());
        }

        user.ifPresent(value -> {
            restriction.setUser(user.get());
            user.get().getRestrictions().add(restriction);
        });

        userRepo.save(user.get());
        restrictionRepo.save(restriction);


        StringBuilder message = new StringBuilder(
                "Здравствуйте, " + user.get().getUsername() + "!\n" +
                        "Услуга \"" + service.get().getName() + "\" успешно добавлена!\n"
        );

        if (service.get().isSubscription()) {
            message.append("Подписка истекает " + new SimpleDateFormat("dd-MM-YYYY").format(restriction.getExpirationDate()) + "\n");
        }

        message.append( "Спасибо! Всего Вам доброго!");

        mailSender.send(user.get().getEmail(),
                "Сообщение о добавлении услуги",
                message.toString());

        return "redirect:/admin/user/" + userId.get() + "/edit";
    }

    /*@GetMapping("/restriction/{restrictionId}/{userId}/remove")
    public String removeRestriction(@PathVariable(value = "restrictionId") Long restrictionId,
                                    @PathVariable(value = "userId") Long userId,
                                  Model model) {
        if (!restrictionRepo.existsById(restrictionId)) {
            return "redirect:/admin/user/" + userId + "/edit";
        }

        Optional<Restriction> restriction = restrictionRepo.findById(restrictionId);

        restrictionRepo.delete(restriction.get());

        return "redirect:/admin/user/" + userId + "/edit";
    }*/

    @GetMapping("/user/{userId}/organizers")
    public String userOrganizers(HttpServletRequest request, Model model,
                                 @PathVariable(value = "userId") Long userId,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse((int) Optional.ofNullable(request.getSession().getAttribute(Constants.SESSION_USER_ORGANIZER_LAST_PAGE)).orElse(1));
        int pageSize = size.orElse(50);
        request.getSession().setAttribute(Constants.SESSION_USER_ORGANIZER_LAST_PAGE, currentPage);

        Optional<User> user = userRepo.findById(userId);


        Page<Organizer> organizerPage = organizerService.findUserPaginated(
                PageRequest.of(currentPage - 1, pageSize),
                user.get());

        model.addAttribute("title", "Администрирование - Органайзеры пользователя");
        model.addAttribute("header", "Органайзеры пользователя " + user.get().getUsername() + " (" + organizerPage.getTotalElements() + ")");

        model.addAttribute("organizerPage", organizerPage);

        int totalPages = organizerPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return  "admin/userOrganizers";
    }
}
