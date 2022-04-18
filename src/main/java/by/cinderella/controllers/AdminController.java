package by.cinderella.controllers;

import by.cinderella.model.organizer.Organizer;
import by.cinderella.model.organizer.OrganizerCategory;
import by.cinderella.repos.OrganizerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private OrganizerRepo organizerRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @ModelAttribute("organizerCategories")
    public Set<OrganizerCategory> populateFeatures() {
        Set<OrganizerCategory> result  = new TreeSet<>();

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
                                @RequestParam("image") MultipartFile image,
                               Model model) throws IOException {

        if (image != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFileName = UUID.randomUUID().toString();
            String resultFileName = uuidFileName + image.getOriginalFilename();

            image.transferTo(new File(uploadPath + "/" + resultFileName));

            organizer.setImageName(resultFileName);
        }

        organizer.setLastUpdated(new Date());
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
        model.addAttribute("title", "Администрирование - Редактировать " + organizer.get().getName());
        model.addAttribute("categories", new HashSet<>());

        return  "admin/editOrganizer";
    }

    @GetMapping("/organizer/{organizerId}/copy")
    public String copyOrganizer(@PathVariable(value = "organizerId") Long organizerId,
                                Model model) {
        if (!organizerRepo.existsById(organizerId)) {
            return "redirect:/admin/organizers";
        }

        Optional<Organizer> organizer = organizerRepo.findById(organizerId);

        organizer.get().setId(null);
        model.addAttribute("organizer", organizer.get());
        model.addAttribute("title", "Администрирование - Редактировать " + organizer.get().getName());
        model.addAttribute("categories", new HashSet<>());

        return  "admin/editOrganizer";
    }

    @PostMapping("/editOrganizer")
    public String editOrganizer(Organizer organizer,

                               Model model) {

        organizer.setLastUpdated(new Date());

        organizerRepo.save(organizer);



        return "redirect:/admin/organizers";
    }
}
