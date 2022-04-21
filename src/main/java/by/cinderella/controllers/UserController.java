package by.cinderella.controllers;

import by.cinderella.model.organizer.*;
import by.cinderella.repos.OrganizerRepo;
import by.cinderella.services.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequestMapping("/user")
@PreAuthorize("hasAnyAuthority('USER, ADMIN')")
public class UserController {
    @Autowired
    private OrganizerRepo organizerRepo;

    @Autowired
    private OrganizerService organizerService;


    @ModelAttribute("organizerCategories")
    public Set<OrganizerCategory> organizerCategories() {
        Set<OrganizerCategory> result  = new TreeSet<>();

        Collections.addAll(result, OrganizerCategory.values());
        return result;
    }

    @GetMapping("")
    public String mainPage() {
        return "user/main";
    }

    @GetMapping("/organizers")
    public String organizerPage(HttpServletRequest request, Model model,
                                @RequestParam("priceFrom") Optional<Double> priceFrom,
                                @RequestParam("priceTo") Optional<Double> priceTo,

                                @RequestParam("categories") Optional<Set<OrganizerCategory>> categories,

                                @RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(50);



        Filter filter = new Filter(
                "",
                (double) 0,
                (double) 0,
                (double) 0,
                (double) 0,
                (double) 0,
                (double) 0,
                priceFrom.orElse((double) 0),
                priceTo.orElse((double) Integer.MAX_VALUE),
                categories.orElse(this.organizerCategories()),
                new TreeSet<Seller>(),
                new TreeSet<Material>()
        );

        Page<Organizer> organizerPage = organizerService.findPaginatedAndFiltered(filter,
                PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("title", "Администрирование - Органайзеры");

        model.addAttribute("organizerPage", organizerPage);

        int totalPages = organizerPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return  "user/organizers";
    }
}
