package by.cinderella.repos;

import by.cinderella.model.user.UserOrganizer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOrganizerRepo extends JpaRepository<UserOrganizer, Long> {
}
