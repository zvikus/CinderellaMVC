package by.cinderella.controllers;

import by.cinderella.model.organizer.OrganizerCategory;
import by.cinderella.repos.RestrictionRepo;
import by.cinderella.repos.ServiceRepo;
import by.cinderella.repos.UserRepo;
import by.cinderella.services.OrganizerService;
import by.cinderella.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class BaseController {

    @ModelAttribute("organizerGroupCategories")
    public Map<OrganizerCategory.ParentCategory, List<OrganizerCategory>> organizerGroupCategories() {
        Set<OrganizerCategory> result  = new TreeSet<>();

        Collections.addAll(result, OrganizerCategory.values());


        Map<OrganizerCategory.ParentCategory, List<OrganizerCategory>> organizerGroupCategories = result.stream()
                .collect(groupingBy(m -> m.parentCategory));

        /*SortedMap<OrganizerCategory.ParentCategory, List<OrganizerCategory>> resultOrganizerGroupCategories = new TreeMap<>();

        resultOrganizerGroupCategories.put(OrganizerCategory.ParentCategory.CABINET,
                organizerGroupCategories.get(OrganizerCategory.ParentCategory.CABINET));
        resultOrganizerGroupCategories.put(OrganizerCategory.ParentCategory.KITCHEN,
                organizerGroupCategories.get(OrganizerCategory.ParentCategory.KITCHEN));
        resultOrganizerGroupCategories.put(OrganizerCategory.ParentCategory.REFRIGERATOR,
                organizerGroupCategories.get(OrganizerCategory.ParentCategory.REFRIGERATOR));
        resultOrganizerGroupCategories.put(OrganizerCategory.ParentCategory.CHILDREN,
                organizerGroupCategories.get(OrganizerCategory.ParentCategory.CHILDREN));
        resultOrganizerGroupCategories.put(OrganizerCategory.ParentCategory.BATHROOM,
                organizerGroupCategories.get(OrganizerCategory.ParentCategory.BATHROOM));
        resultOrganizerGroupCategories.put(OrganizerCategory.ParentCategory.HALLWAY,
                organizerGroupCategories.get(OrganizerCategory.ParentCategory.HALLWAY));
        resultOrganizerGroupCategories.put(OrganizerCategory.ParentCategory.BALCONY,
                organizerGroupCategories.get(OrganizerCategory.ParentCategory.BALCONY));
        resultOrganizerGroupCategories.put(OrganizerCategory.ParentCategory.OTHER,
                organizerGroupCategories.get(OrganizerCategory.ParentCategory.OTHER));*/

        return organizerGroupCategories;
    }

    @ModelAttribute("categoryKeys")
    public List<OrganizerCategory.ParentCategory> categoryKeys() {
        List<OrganizerCategory.ParentCategory> result  = new ArrayList<>();

        result.add(OrganizerCategory.ParentCategory.CABINET);
        result.add(OrganizerCategory.ParentCategory.KITCHEN);
        result.add(OrganizerCategory.ParentCategory.REFRIGERATOR);
        result.add(OrganizerCategory.ParentCategory.CHILDREN);
        result.add(OrganizerCategory.ParentCategory.BATHROOM);
        result.add(OrganizerCategory.ParentCategory.HALLWAY);
        result.add(OrganizerCategory.ParentCategory.BALCONY);
        result.add(OrganizerCategory.ParentCategory.DOCUMENTS);
        result.add(OrganizerCategory.ParentCategory.OTHER);

        return result;
    }

    @Autowired
    UserRepo userRepo;
    @Autowired
    ServiceRepo serviceRepo;
    @Autowired
    RestrictionRepo restrictionRepo;
    @Autowired
    UserService userService;
    @Autowired
    OrganizerService organizerService;

}
