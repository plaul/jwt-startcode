package kea.sem3.jwtdemo.security.dto;

import kea.sem3.jwtdemo.security.User;
import kea.sem3.jwtdemo.security.UserDetailsImp;
import kea.sem3.jwtdemo.security.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    UserRepository userRepository;

    public UserDetailsServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.map(UserDetailsImp::new).orElseThrow(() -> new BadCredentialsException(""));
    }

}
