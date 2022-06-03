package by.cinderella.repos;

import by.cinderella.model.user.Restriction;
import by.cinderella.model.user.Service;
import by.cinderella.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestrictionRepo extends JpaRepository<Restriction, Long> {
    List<Restriction> findAllByUserAndService(User user, Optional<Service> service);
}
