package by.cinderella.services;

import by.cinderella.model.organizer.Filter;
import by.cinderella.model.organizer.Organizer;
import by.cinderella.model.organizer.OrganizerSpecification;
import by.cinderella.repos.OrganizerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class OrganizerService {
    @Autowired
    private OrganizerRepo organizerRepo;

    public Page<Organizer> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();

        Page<Organizer> organizerPage
                = organizerRepo.findAll(PageRequest.of(currentPage, pageSize,
                        Sort.by("lastUpdated").descending().and(Sort.by("name"))));

        return organizerPage;
    }

    /*public Page<Organizer> findPaginatedAndFiltered(Double priceFrom,
                                                    Double priceTo,
                                                    Pageable pageable){
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();

        Page<Organizer> organizerPage
                = organizerRepo.findAllByPriceBetween(priceFrom, priceTo, PageRequest.of(currentPage, pageSize,
                Sort.by("lastUpdated").descending().and(Sort.by("name"))));

        return organizerPage;
    }*/

    public Page<Organizer> findPaginatedAndFiltered(Filter filter,
                                                    Pageable pageable) {

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();

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

                        PageRequest.of(currentPage, pageSize, Sort.by("lastUpdated").descending().and(Sort.by("name"))));

        return organizerPage;
    }

}
