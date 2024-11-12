package by.cinderella.repos;

import by.cinderella.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findTopByUsername(String username);

    User findTopByEmail(String email);

    User findByEmailAndActiveTrue(String email);

    User findByActivationCode(String code);
}
