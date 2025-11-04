package linkProject.repozitories;

import linkProject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findUserByUUID(String UUID);
}
