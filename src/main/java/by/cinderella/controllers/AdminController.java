package by.cinderella.controllers;

import by.cinderella.model.organizer.Organizer;
import by.cinderella.model.organizer.OrganizerCategory;
import by.cinderella.repos.OrganizerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private OrganizerRepo organizerRepo;

    @ModelAttribute("organizerCategories")
    public Set<OrganizerCategory> populateFeatures() {
        Set<OrganizerCategory> result  = new HashSet<>();
        Collections.addAll(result, OrganizerCategory.values());
        return result;
    }


    @GetMapping("")
    public String mainPage(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Администрирование");

        return  "admin/main";
    }

    @GetMapping("/about")
    public String aboutPage(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Администрирование");

        return  "admin/main";
    }

    @GetMapping("/organizers")
    public String organizerPage(HttpServletRequest request, Model model) {
        Iterable<Organizer> organizers = organizerRepo.findAll();

        model.addAttribute("title", "Администрирование - Органайзеры");

        model.addAttribute("organizers", organizers);
        return  "admin/organizers";
    }


    @GetMapping("/addOrganizer")
    public String addOrganizerForm(Model model) {//
        model.addAttribute("title", "Администрирование - Добавить органайзер");
        model.addAttribute("organizer", new Organizer());
        model.addAttribute("categories", new HashSet<>());

        return  "admin/addOrganizer";
    }


    @PostMapping("/addOrganizer")
    public String addOrganizer(Organizer organizer,

                               Model model) {

        organizerRepo.save(organizer);

        return "redirect:/admin/organizers";
    }

    @GetMapping("/organizer/{organizerId}/remove")
    public String removeOrganizer(@PathVariable(value = "organizerId") Long organizerId,
                                Model model) {
        if (!organizerRepo.existsById(organizerId)) {
            return "redirect:/admin/organizers";
        }

        Optional<Organizer> organizer = organizerRepo.findById(organizerId);

        organizerRepo.delete(organizer.get());

        return "redirect:/admin/organizers";
    }

    @GetMapping("/organizer/{organizerId}/edit")
    public String editOrganizer(@PathVariable(value = "organizerId") Long organizerId,
                                  Model model) {
        if (!organizerRepo.existsById(organizerId)) {
            return "redirect:/admin/organizers";
        }

        Optional<Organizer> organizer = organizerRepo.findById(organizerId);

        model.addAttribute("organizer", organizer.get());

        return  "admin/addOrganizer";
    }
}
