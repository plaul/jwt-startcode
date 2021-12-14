package kea.sem3.jwtdemo.security;

import kea.sem3.jwtdemo.security.Role;
import kea.sem3.jwtdemo.security.User;
import kea.sem3.jwtdemo.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SetupTestUsers {

    public static void addTestUsers(UserRepository userRepository) {
        User user = new User("user", "user@a.dk", "test12");
        user.addRole(Role.USER);
        User admin = new User("admin", "admin@a.dk","test12");
        admin.addRole(Role.ADMIN);

        User both = new User("user_admin", "both@a.dk", "test12");
        both.addRole(Role.USER);
        both.addRole(Role.ADMIN);

        userRepository.save(user);
        userRepository.save(admin);
        userRepository.save(both);
    }
}
