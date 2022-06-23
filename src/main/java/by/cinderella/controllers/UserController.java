package by.cinderella.controllers;

import by.cinderella.config.Constants;
import by.cinderella.model.organizer.*;
import by.cinderella.model.user.OrganizerList;
import by.cinderella.model.user.User;
import by.cinderella.model.user.UserOrganizer;
import by.cinderella.repos.OrganizerListRepo;
import by.cinderella.repos.OrganizerRepo;
import by.cinderella.repos.UserOrganizerRepo;
import by.cinderella.repos.UserRepo;
import by.cinderella.services.OrganizerPDFExporter;
import by.cinderella.services.OrganizerService;
import by.cinderella.services.UserService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequestMapping("/user")
@PreAuthorize("hasAnyAuthority('USER, ADMIN, SADMIN')")
public class UserController {
    @Value("${organizer.search.service.id}")
    private Integer searchServiceId;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrganizerListRepo organizerListRepo;

    @Autowired
    private UserOrganizerRepo userOrganizerRepo;

    @Autowired
    private OrganizerRepo organizerRepo;

    @Autowired
    private OrganizerService organizerService;

    @Autowired
    private UserService userService;

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

    @GetMapping("/userOrganizers")
    public String getUserOrganizers(HttpServletRequest request, Model model) {
        if (!userService.checkUserRestriction((long) searchServiceId)) {
            return "redirect:/user";
        }

        model.addAttribute("organizerList", userService.getAuthUser().getOrganizerLists());

        return  "user/userOrganizers";
    }

    @GetMapping("/userOrganizers/{organizerListId}/pdf.pdf")
    public void exportToPDF(HttpServletResponse response,
                            @PathVariable("organizerListId") Long organizerListId)
            throws IOException, DocumentException, URISyntaxException {
        Optional<OrganizerList> organizerList = organizerListRepo.findById(organizerListId);
        if (organizerList.isPresent()) {
            OrganizerPDFExporter exporter = new OrganizerPDFExporter(uploadPath, organizerList.get());

            exporter.export(response);
        }
    }

    @GetMapping("/userOrganizers/{organizerListId}/remove")
    public String removeOrganizerList(HttpServletRequest request, Model model,
                                      @PathVariable("organizerListId") Long organizerListId) {
        Optional<OrganizerList> organizerList = organizerListRepo.findById(organizerListId);
        if (organizerList.isPresent()) {
            User user = userService.getAuthUser();
            Set<OrganizerList> userOrganizerList = user.getOrganizerLists();
            userOrganizerList.remove(organizerList.get());
            user.setOrganizerLists(userOrganizerList);
            userRepo.save(user);
            for(UserOrganizer userOrganizer: organizerList.get().getUserOrganizerList()) {
                userOrganizerRepo.delete(userOrganizer);
            }
            organizerListRepo.delete(organizerList.get());
        }
        return "redirect:/user/userOrganizers";
    }
    @GetMapping("/userOrganizersList/{organizerListId}/{userOrganizerId}/remove")
    public String removeOrganizerFromOrganizerList(HttpServletRequest request, Model model,
                                                   @PathVariable("organizerListId") Long organizerListId,
                                                   @PathVariable("userOrganizerId") Long userOrganizerId) {
        Optional<OrganizerList> organizerList = organizerListRepo.findById(organizerListId);
        Optional<UserOrganizer> userOrganizer = userOrganizerRepo.findById(userOrganizerId);

        if (userOrganizer.isPresent()
            && organizerList.isPresent()) {
            Set<UserOrganizer> organizers = organizerList.get().getUserOrganizerList();

            organizers.remove(userOrganizer.get());
            userOrganizer.get().setOrganizer(null);
            organizerList.get().setUserOrganizerList(organizers);
            organizerList.get().setLastUpdated(new Date());

            userOrganizerRepo.delete(userOrganizer.get());
            organizerListRepo.save(organizerList.get());

        }
        return "redirect:/user/userOrganizersList/" + organizerListId + "/show";
    }


    @PostMapping("/userOrganizer/{organizerListId}/edit")
    public String editUserOrganizer(HttpServletRequest request, Model model,
                                    @RequestParam("count") Optional<Double> count,
                                    @RequestParam("comment") Optional<String> comment,
                                        @PathVariable("organizerListId") Long organizerId) {
        Optional<UserOrganizer> userOrganizer = userOrganizerRepo.findById(organizerId);
        if (userOrganizer.isPresent()) {
            userOrganizer.get().setCount((int) Math.round(count.get()));
            userOrganizer.get().setComment(comment.get());
            userOrganizerRepo.save(userOrganizer.get());
            return "redirect:/user/userOrganizersList/" + userOrganizer.get().getOrganizerList().getId() + "/show";
        } else {
            return "redirect:/user/userOrganizers";
        }


    }

    @PostMapping("/newOrganizerList")
    public String addUserOrganizerList(OrganizerList organizerList,
            HttpServletRequest request, Model model) {
        if (!userService.checkUserRestriction((long) searchServiceId)) {
            return "redirect:/user";
        }

        User user = userService.getAuthUser();
        organizerList.setCreated(new Date());
        organizerList.setUser(user);

        user.getOrganizerLists().add(organizerList);

        userRepo.save(user);
        organizerListRepo.save(organizerList);

        return "redirect:/user/userOrganizers";
    }

    @PostMapping("/addToUserList/{organizerId}/{organizerListId}")
    public ResponseEntity<String> addOrganizerToUserList(HttpServletRequest request, Model model,
                                                 @PathVariable("organizerId") Long organizerId,
                                                 @PathVariable("organizerListId") Long organizerListId) {
        Optional<OrganizerList> organizerList = organizerListRepo.findById(organizerListId);
        Optional<Organizer> organizer = organizerRepo.findById(organizerId);

        if (organizerList.isPresent()
            && organizer.isPresent()) {
            if (!organizerList.get().getUserOrganizerList().contains(organizer.get())) {
                UserOrganizer userOrganizer = new UserOrganizer();
                userOrganizer.setOrganizer(organizer.get());
                userOrganizer.setCount(1);
                userOrganizer.setComment("");
                userOrganizer.setOrganizerList(organizerList.get());
                organizerList.get().getUserOrganizerList().add(userOrganizer);
                organizerList.get().setLastUpdated(new Date());

                userOrganizerRepo.save(userOrganizer);
                organizerListRepo.save(organizerList.get());
            } else {
                return new ResponseEntity<String>(HttpStatus.ALREADY_REPORTED);
            }
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @GetMapping("/userOrganizersList/{organizerListId}/show")
    public String getUserOrganizersList(HttpServletRequest request, Model model,
                                                    @PathVariable("organizerListId") Long organizerListId) {
        Optional<OrganizerList> organizerList = organizerListRepo.findById(organizerListId);

        model.addAttribute("organizerList", organizerList.get());

        return "user/userOrganizersList";
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
                                @RequestParam("size") Optional<Integer> size,

                                @RequestParam("sortingField") Optional<String> sortingField,
                                @RequestParam("sortDirection") Optional<String> sortDirectionString
                                ) {

        if (!userService.checkUserRestriction((long) searchServiceId)) {
            return "redirect:/user";
        }

        int currentPage = page.orElse((int) Optional.ofNullable(request.getSession().getAttribute(Constants.SESSION_ORGANIZER_LAST_PAGE)).orElse(1));
        int pageSize = size.orElse(50);

        request.getSession().setAttribute(Constants.SESSION_ORGANIZER_LAST_PAGE, currentPage);

        Filter filter;

        if (request.getSession().getAttribute(Constants.SESSION_ORGANIZER_FILTER) != null) {
            Filter filterFromSession = (Filter) request.getSession().getAttribute(Constants.SESSION_ORGANIZER_FILTER);

            Sort.Direction sortDirection = sortDirectionString.map(Sort.Direction::fromString).orElseGet(filterFromSession::getSortDirection);

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
                    materials.orElse(filterFromSession.getMaterial()),

                    sortingField.orElse(filterFromSession.getSortingField()),
                    sortDirection

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
                    materials.orElse(null),


                    sortingField.orElse("lastUpdated"),
                    Sort.Direction.DESC
            );
        }

        request.getSession().setAttribute(Constants.SESSION_ORGANIZER_FILTER, filter);

        Page<Organizer> organizerPage = organizerService.findPaginatedAndFiltered(filter,
                PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("title", "Администрирование - Органайзеры");
        model.addAttribute("filter", filter);

        model.addAttribute("organizerPage", organizerPage);

        model.addAttribute("organizerLists", userService.getAuthUser().getOrganizerLists());

        int totalPages = organizerPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return  "user/organizers";
    }

    @GetMapping("/destroyFilter")
    public String destroySession(HttpServletRequest request) {
        request.getSession().setAttribute(Constants.SESSION_ORGANIZER_FILTER, null);

        return "redirect:/user/organizers";
    }
}
