package by.cinderella.services;

import by.cinderella.model.currency.Currency;
import by.cinderella.model.organizer.*;
import by.cinderella.repos.OrganizerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class OrganizerService {
    @Autowired
    private OrganizerRepo organizerRepo;

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



    public Page<Organizer> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();

        Page<Organizer> organizerPage
                = organizerRepo.findAll(PageRequest.of(currentPage, pageSize,
                        Sort.by("lastUpdated").descending().and(Sort.by("name"))));

        return organizerPage;
    }

    public Page<Organizer> findPaginatedAndFiltered(Filter userFilter,
                                                    Pageable pageable) {

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();

        Filter filter = this.initFilter(userFilter);

        Currency currency = userService.getUserCurrency();

        Page<Organizer> organizerPage =
                //organizerRepo.findAllByPriceBetween(priceFrom, priceTo,
                organizerRepo.findAll(
                        OrganizerSpecification.nameLike(filter.getNameLike())

                                .and(OrganizerSpecification.priceBounds(filter.getPriceFrom(), filter.getPriceTo()))
                                .and(OrganizerSpecification.widthBounds(filter.getWidthFrom(), filter.getWidthTo()))
                                .and(OrganizerSpecification.heightBounds(filter.getHeightFrom(), filter.getHeightTo()))
                                .and(OrganizerSpecification.lengthBounds(filter.getLengthFrom(), filter.getLengthTo()))

                                .and(OrganizerSpecification.categoriesIn(filter.getCategories()))
                                .and(OrganizerSpecification.materialIn(filter.getMaterial()))
                                .and(OrganizerSpecification.sellersIn(filter.getSeller()))
                        ,

                        PageRequest.of(currentPage, pageSize, Sort.by(filter.getSortDirection(), filter.getSortingField()).and(Sort.by("name"))));


        return organizerPage;
    }

    public Page<Organizer> findUserPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        Page<Organizer> organizerPage =
                organizerRepo.findByCreatedBy(
                        userService.getAuthUser(),
                        PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "lastUpdated").and(Sort.by("name"))));

        return organizerPage;
    }

    private Filter initFilter(Filter userFilter) {
        Filter filter = new Filter();

        if (userFilter.getNameLike() != null) {
            filter.setNameLike(userFilter.getNameLike().replaceAll(" ", "%"));
        } else {
            filter.setNameLike("");
        }

        if (userFilter.getLengthFrom() != null) {
            filter.setLengthFrom(userFilter.getLengthFrom());
        } else {
            filter.setLengthFrom((double) 0);
        }
        if (userFilter.getWidthFrom() != null) {
            filter.setWidthFrom(userFilter.getWidthFrom());
        } else {
            filter.setWidthFrom((double) 0);
        }
        if (userFilter.getHeightFrom() != null) {
            filter.setHeightFrom(userFilter.getHeightFrom());
        } else {
            filter.setHeightFrom((double) 0);
        }


        if (userFilter.getLengthTo() != null) {
            filter.setLengthTo(userFilter.getLengthTo());
        } else {
            filter.setLengthTo((double) Integer.MAX_VALUE);
        }
        if (userFilter.getWidthTo() != null) {
            filter.setWidthTo(userFilter.getWidthTo());
        } else {
            filter.setWidthTo((double) Integer.MAX_VALUE);
        }
        if (userFilter.getHeightTo() != null) {
            filter.setHeightTo(userFilter.getHeightTo());
        } else {
            filter.setHeightTo((double) Integer.MAX_VALUE);
        }

        if (userFilter.getPriceFrom() != null) {
            filter.setPriceFrom(
                    CurrencyRateService.getAbsolutePrice(userFilter.getPriceFrom(), userService.getUserCurrency())
            );
        } else {
            filter.setPriceFrom((double) 0);
        }
        if (userFilter.getPriceTo() != null) {
            filter.setPriceTo(
                    CurrencyRateService.getAbsolutePrice(userFilter.getPriceTo(), userService.getUserCurrency())
            );
        } else {
            filter.setPriceTo((double) Integer.MAX_VALUE);
        }


        if (userFilter.getCategories() != null) {
            filter.setCategories(userFilter.getCategories());
        } else {
            filter.setCategories(this.organizerCategories());
        }
        if (userFilter.getSeller() != null) {
            filter.setSeller(userFilter.getSeller());
        } else {
            filter.setSeller(this.organizerSellers());
        }
        if (userFilter.getMaterial() != null) {
            filter.setMaterial(userFilter.getMaterial());
        } else {
            filter.setMaterial(this.organizerMaterials());
        }

        filter.setSortingField(userFilter.getSortingField());
        filter.setSortDirection(userFilter.getSortDirection());

        return filter;
    }

    public List<Organizer> getWildberriesOrganizers() {
        return organizerRepo.findBySeller(Seller.WB);
    }

    public void save(Organizer organizer) {
        organizer.setAbsolutePrice(CurrencyRateService.getAbsolutePrice(organizer));
        organizerRepo.save(organizer);
    }
}
