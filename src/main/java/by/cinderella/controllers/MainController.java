package by.cinderella.controllers;

import by.cinderella.config.Constants;
import by.cinderella.model.currency.Currency;
import by.cinderella.model.organizer.Filter;
import by.cinderella.model.user.Restriction;
import by.cinderella.model.user.Service;
import by.cinderella.model.user.User;
import by.cinderella.repos.RestrictionRepo;
import by.cinderella.repos.ServiceRepo;
import by.cinderella.repos.UserRepo;
import by.cinderella.services.CinderellaMailSender;
import by.cinderella.services.CurrencyRateService;
import by.cinderella.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Controller
public class MainController extends BaseController {
    @GetMapping("")
    public String mainPage(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Главная");

        return  "index";
    }

    @Autowired
    private CinderellaMailSender mailSender;

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

    @GetMapping("/organizersServiceDetails")
    public String organizersServiceDetails(HttpServletRequest request, Model model) {
        Currency userCurrency = Currency.BYN;
        if (request.getSession().getAttribute("currency") != null) {
            userCurrency = (Currency) request.getSession().getAttribute("currency");
        }
        Service service = serviceRepo.findById(searchServiceId).get();
        model.addAttribute("service", service);
        model.addAttribute("userCurrency", userCurrency);

        model.addAttribute("userServiceCost",
                CurrencyRateService.convert(service.getCost(),
                        Currency.BYN,
                        userCurrency) + " " + userCurrency.CUR_ABBREVIATION);

        model.addAttribute("userServiceCost1",
                CurrencyRateService.convert(service.getCost1(),
                        Currency.BYN,
                        userCurrency) + " " + userCurrency.CUR_ABBREVIATION);

        model.addAttribute("userServiceCost3",
                CurrencyRateService.convert(service.getCost3(),
                        Currency.BYN,
                        userCurrency) + " " + userCurrency.CUR_ABBREVIATION);

        model.addAttribute("userServiceCost6",
                CurrencyRateService.convert(service.getCost6(),
                        Currency.BYN,
                        userCurrency) + " " +  userCurrency.CUR_ABBREVIATION);
        model.addAttribute("userServiceCost12",
                CurrencyRateService.convert(service.getCost12(),
                        Currency.BYN,
                        userCurrency) + " " + userCurrency.CUR_ABBREVIATION);
        return "user/organizersServiceDetails";
    }


    @GetMapping("/changeCurrency/{currencyName}/{serviceId}")
    public String changeCurrency(HttpServletRequest request, Model model,
                                 @PathVariable("currencyName") String currencyName,
                                 @PathVariable("serviceId") String serviceId){
        Currency currency = Currency.valueOf(currencyName);
        if (currency!=null) {
            if (request.getSession().getAttribute(Constants.SESSION_ORGANIZER_FILTER) != null) {
                Filter filterFromSession = (Filter) request.getSession().getAttribute(Constants.SESSION_ORGANIZER_FILTER);
                filterFromSession.setPriceFrom(null);
                filterFromSession.setPriceTo(null);
                request.getSession().setAttribute(Constants.SESSION_ORGANIZER_FILTER, filterFromSession);
            }
            request.getSession().setAttribute("currency", currency);
        }
        if (serviceId.equals(searchServiceId)) {
            return "redirect:/organizersServiceDetails";
        } else {
            return "redirect:/userService/" + serviceId + "/buy";
        }
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

            restriction.setOperationId(operationId);

            if (service.get().isSubscription()) {
                Integer term = new Integer(params[2]);

                Date serviceExpirationDate = userService.getServiceExpirationDate(serviceId, userId);
                if (serviceExpirationDate == null || serviceExpirationDate.before(new Date())) {
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

            //TODO: refactor: move to service
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
        } else {
            return ResponseEntity.badRequest().build();
        }



        return ResponseEntity.ok().build();
    }

    @GetMapping("/userService/{serviceId}/buy")
    public String userServiceBuy(HttpServletRequest request, Model model,
                                 @PathVariable("serviceId") Long serviceId) {
        Currency userCurrency;
        if (userService.getAuthUser() != null) {
            userCurrency = userService.getAuthUser().getCurrency();
        } else {
            userCurrency = Currency.BYN;
            if (request.getSession().getAttribute("currency") != null) {
                userCurrency = (Currency) request.getSession().getAttribute("currency");
            }
        }
        Service service = serviceRepo.findById(serviceId).get();
        model.addAttribute("service", service);
        model.addAttribute("userCurrency", userCurrency);

        model.addAttribute("userServiceCost",
                CurrencyRateService.convert(service.getCost(),
                        Currency.BYN,
                        userCurrency) + " " + userCurrency.CUR_ABBREVIATION);
        model.addAttribute("serviceCostRub",
                CurrencyRateService.convert(service.getCost(),
                        Currency.BYN,
                        Currency.RUB));
        model.addAttribute("userServiceCost1",
                CurrencyRateService.convert(service.getCost1(),
                        Currency.BYN,
                        userCurrency) + " " + userCurrency.CUR_ABBREVIATION);
        model.addAttribute("serviceCost1Rub",
                CurrencyRateService.convert(service.getCost1(),
                        Currency.BYN,
                        Currency.RUB));
        model.addAttribute("userServiceCost3",
                CurrencyRateService.convert(service.getCost3(),
                        Currency.BYN,
                        userCurrency) + " " + userCurrency.CUR_ABBREVIATION);
        model.addAttribute("serviceCost3Rub",
                CurrencyRateService.convert(service.getCost3(),
                        Currency.BYN,
                        Currency.RUB));
        model.addAttribute("userServiceCost6",
                CurrencyRateService.convert(service.getCost6(),
                        Currency.BYN,
                        userCurrency) + " " +  userCurrency.CUR_ABBREVIATION);
        model.addAttribute("serviceCost6Rub",
                CurrencyRateService.convert(service.getCost6(),
                        Currency.BYN,
                        Currency.RUB));
        model.addAttribute("userServiceCost12",
                CurrencyRateService.convert(service.getCost12(),
                        Currency.BYN,
                        userCurrency) + " " + userCurrency.CUR_ABBREVIATION);
        model.addAttribute("serviceCost12Rub",
                CurrencyRateService.convert(service.getCost12(),
                        Currency.BYN,
                        Currency.RUB));

        return "user/userServiceBuy";
    }
}
