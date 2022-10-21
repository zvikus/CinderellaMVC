package by.cinderella.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/services")
public class ServicesController {
    @GetMapping("/order")
    public String orderPage(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Порядок в доме");

        return  "main/services/order";
    }

    @GetMapping("/design")
    public String designPage(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Проекты мебели");

        return  "main/services/design";
    }

    @GetMapping("/order/exitService")
    public String departurePage(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Выездная организация");

        return  "main/services/order/exitService";
    }
    @GetMapping("/order/consulting")
    public String consultingOrderPage(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Консультация");

        return  "main/services/order/consulting";
    }
    @GetMapping("/order/relocation")
    public String movementPage(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Переезд");

        return  "main/services/order/relocation";
    }

    @GetMapping("/order/online")
    public String onlinePage(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Порядок онлайн");

        return  "main/services/order/online";
    }

    @GetMapping("/design/cabinet")
    public String cabinetPage(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Проектирование шкафов");

        return  "main/services/design/cabinet";
    }
    @GetMapping("/design/audit")
    public String auditPage(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Аудит");

        return  "main/services/design/audit";
    }

    @GetMapping("/design/kitchen")
    public String kitchenPage(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Проектирование кухни");

        return  "main/services/design/kitchen";
    }

    @GetMapping("/design/arrangement")
    public String arrangementPage(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Проектирование шкафов");

        return  "main/services/design/arrangement";
    }
}
