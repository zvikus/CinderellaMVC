package by.cinderella.repos;

import by.cinderella.model.User;
import by.cinderella.model.organizer.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizerRepo extends JpaRepository<Organizer, Long> {
    
}
