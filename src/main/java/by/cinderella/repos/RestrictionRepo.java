package by.cinderella.repos;

import by.cinderella.model.user.Restriction;
import by.cinderella.model.user.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestrictionRepo extends JpaRepository<Restriction, Long> {
}
