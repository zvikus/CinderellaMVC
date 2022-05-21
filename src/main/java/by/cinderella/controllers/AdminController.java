package by.cinderella.controllers;

import by.cinderella.config.Constants;
import by.cinderella.model.User;
import by.cinderella.model.organizer.*;
import by.cinderella.repos.OrganizerRepo;
import by.cinderella.services.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private OrganizerRepo organizerRepo;

    @Autowired
    private OrganizerService organizerService;

    @Value("${upload.path}")
    private String uploadPath;

    @ModelAttribute("organizerCategories")
    public Set<OrganizerCategory> organizerCategories() {
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
    public String organizerPage(HttpServletRequest request, Model model,
                                @RequestParam("name") Optional<String> name,

                                @RequestParam("priceFrom") Optional<Double> priceFrom,
                                @RequestParam("priceTo") Optional<Double> priceTo,


                                @RequestParam("lengthFrom") Optional<Double> lengthFrom,
                                @RequestParam("lengthTo") Optional<Double> lengthTo,
                                @RequestParam("widthFrom") Optional<Double> widthFrom,
                                @RequestParam("widthTo") Optional<Double> widthTo,
                                @RequestParam("heightFrom") Optional<Double> heightFrom,
                                @RequestParam("heightTo") Optional<Double> heightTo,

                                @RequestParam("categories") Optional<Set<OrganizerCategory>> categories,
                                @RequestParam("sellers") Optional<Set<Seller>> sellers,
                                @RequestParam("materials") Optional<Set<Material>> materials,

                                @RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse((int) Optional.ofNullable(request.getSession().getAttribute(Constants.SESSION_ORGANIZER_LAST_PAGE)).orElse(1));
        int pageSize = size.orElse(50);
        request.getSession().setAttribute(Constants.SESSION_ORGANIZER_LAST_PAGE, currentPage);

        Filter filter;

        if (request.getSession().getAttribute(Constants.SESSION_ORGANIZER_FILTER) != null) {
            Filter filterFromSession = (Filter) request.getSession().getAttribute(Constants.SESSION_ORGANIZER_FILTER);
            filter = new Filter(
                    name.orElse(filterFromSession.getNameLike()),

                    lengthFrom.orElse(filterFromSession.getLengthFrom()),
                    widthFrom.orElse(filterFromSession.getWidthFrom()),
                    heightFrom.orElse(filterFromSession.getHeightFrom()),

                    lengthTo.orElse(filterFromSession.getLengthTo()),
                    widthTo.orElse(filterFromSession.getWidthTo()),
                    heightTo.orElse(filterFromSession.getHeightTo()),

                    priceFrom.orElse(filterFromSession.getPriceFrom()),
                    priceTo.orElse(filterFromSession.getPriceTo()),

                    categories.orElse(filterFromSession.getCategories()),
                    sellers.orElse(filterFromSession.getSeller()),
                    materials.orElse(filterFromSession.getMaterial())
            );
        } else {
            filter = new Filter(
                    name.orElse(null),

                    lengthFrom.orElse(null),
                    widthFrom.orElse(null),
                    heightFrom.orElse(null),

                    lengthTo.orElse(null),
                    widthTo.orElse(null),
                    heightTo.orElse(null),

                    priceFrom.orElse(null),
                    priceTo.orElse(null),

                    categories.orElse(null),
                    sellers.orElse(null),
                    materials.orElse(null)
            );
        }

        request.getSession().setAttribute(Constants.SESSION_ORGANIZER_FILTER, filter);

        Page<Organizer> organizerPage = organizerService.findPaginatedAndFiltered(filter,
                PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("title", "Администрирование - Органайзеры");
        model.addAttribute("filter", filter);
        model.addAttribute("organizerPage", organizerPage);

        int totalPages = organizerPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

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

        List<Organizer> organizerFromDB = organizerRepo.findByLink(organizer.getLink());

        if (!organizerFromDB.isEmpty()) {
            model.addAttribute("message", "Органайзер с такой ссылкой уже существует!");
            model.addAttribute("organizer", organizer);
            model.addAttribute("categories", new HashSet<>());
            return "admin/addOrganizer";
        }


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

    @PostMapping("/checkOrganizer")
    public String checkOrganizer(Organizer organizer,
                               Model model) throws IOException {

        List<Organizer> organizerFromDB = organizerRepo.findByLink(organizer.getLink());

        if (!organizerFromDB.isEmpty()) {
            model.addAttribute("message", "Органайзер с такой ссылкой уже существует!");
            model.addAttribute("organizer", new Organizer());
            model.addAttribute("categories", new HashSet<>());
            model.addAttribute("message", "Органайзер с такой ссылкой уже существует!");
            return "admin/addOrganizer";
        } else {
            model.addAttribute("message", "Органайзер с такой ссылкой уже существует!");
            model.addAttribute("organizer", new Organizer());
            model.addAttribute("categories", new HashSet<>());
            model.addAttribute("message", "Органайзер с такой ссылкой в системе не найден!");
            return "admin/addOrganizer";
        }
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
                                @RequestParam("image") MultipartFile image,
                               Model model) throws IOException {
        if (image != null
                && !image.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFileName = UUID.randomUUID().toString();
            String resultFileName = uuidFileName + image.getOriginalFilename();

            image.transferTo(new File(uploadPath + "/" + resultFileName));

            organizer.setImageName(resultFileName);
        } else if (organizer.getId() != null) {
            Optional<Organizer> originalOrganizer = organizerRepo.findById(organizer.getId());
            organizer.setImageName(originalOrganizer.get().getImageName());
        }

        organizer.setLastUpdated(new Date());

        organizerRepo.save(organizer);



        return "redirect:/admin/organizers";
    }
}
