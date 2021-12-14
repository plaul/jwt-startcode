package kea.sem3.jwtdemo.security.config;

import kea.sem3.jwtdemo.security.Role;
import kea.sem3.jwtdemo.security.User;
import kea.sem3.jwtdemo.security.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 NEVER NEVER USER THIS ON A PUBLIC SERVER
 */

@Configuration
@Profile("!test")
public class DangerouslySetupTestUsers implements CommandLineRunner {

    PasswordEncoder encoder;
    UserRepository userRepository;

    public DangerouslySetupTestUsers(PasswordEncoder encoder, UserRepository userRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {


        User user = new User("user", "user@a.dk", "test12");
        user.addRole(Role.USER);
        User admin = new User("admin", "admin@a.dk", "test12");
        admin.addRole(Role.ADMIN);

        User both = new User("user_admin", "both@a.dk", "test12");
        both.addRole(Role.USER);
        both.addRole(Role.ADMIN);

        userRepository.save(user);
        userRepository.save(admin);
        userRepository.save(both);

        System.out.println("########################################################################################");
        System.out.println("########################################################################################");
        System.out.println("#################################### WARNING ! #########################################");
        System.out.println("## This part breaks a fundamental security rule -> NEVER ship code with default users ##");
        System.out.println("########################################################################################");
        System.out.println("########################  REMOVE BEFORE DEPLOYMENT  ####################################");
        System.out.println("########################################################################################");
        System.out.println("########################################################################################");
        System.out.println("Created TEST Users");
    }
}