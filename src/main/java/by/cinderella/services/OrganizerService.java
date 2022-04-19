package by.cinderella.services;

import by.cinderella.model.organizer.Organizer;
import by.cinderella.repos.OrganizerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class OrganizerService {
    @Autowired
    private OrganizerRepo organizerRepo;

    public Page<Organizer> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        Page<Organizer> organizerPage
                = organizerRepo.findAll(PageRequest.of(currentPage, pageSize,
                        Sort.by("lastUpdated").descending().and(Sort.by("name"))));

        return organizerPage;
    }
}
