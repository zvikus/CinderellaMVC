package by.cinderella.controllers;

import by.cinderella.config.Constants;
import by.cinderella.model.organizer.*;
import by.cinderella.repos.OrganizerRepo;
import by.cinderella.services.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @ModelAttribute("organizerSellers")
    public Set<Seller> organizerSellers() {
        Set<Seller> result  = new TreeSet<>();

        Collections.addAll(result, Seller.values());
        return result;
    }

    @ModelAttribute("organizerMaterials")
    public Set<Material> organizerMaterials() {
        Set<Material> result  = new TreeSet<>();

        Collections.addAll(result, Material.values());
        return result;
    }

    @GetMapping("")
    public String mainPage() {
        return "user/main";
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

        return  "user/organizers";
    }

    @PostMapping("/destroyFilter")
    public String destroySession(HttpServletRequest request) {
        request.getSession().setAttribute(Constants.SESSION_ORGANIZER_FILTER, null);

        return "redirect:/user/organizers";
    }
}
