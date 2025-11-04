package linkProject.services;

import linkProject.models.User;
import linkProject.repozitories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public UserService() {}

    public void save(User user) {
        userRepo.save(user);
    }

    public User findByUuid(String userUuid) {
        return userRepo.findUserByUUID(userUuid);
    }

}
