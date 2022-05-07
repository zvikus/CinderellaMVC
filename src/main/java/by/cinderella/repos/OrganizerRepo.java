package by.cinderella.repos;

import by.cinderella.model.User;
import by.cinderella.model.organizer.Organizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrganizerRepo extends JpaRepository<Organizer, Long>,
        JpaSpecificationExecutor<Organizer> {


    public Page<Organizer> findAllByPriceBetween(Double bottom,
                                                 Double top, Pageable pageable);

    public Page<Organizer> findAll(Specification<Organizer> spec, Pageable pageable);

    public List<Organizer> findByLink(String link);
}
