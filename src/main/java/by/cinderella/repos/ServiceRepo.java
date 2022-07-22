package by.cinderella.repos;

import by.cinderella.model.user.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepo extends JpaRepository<Service, Long> {
    Optional<Service> findById(Long id);
    List<Service> findByPublishedTrue();
}
