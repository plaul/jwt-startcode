package kea.sem3.jwtdemo.security;


import kea.sem3.jwtdemo.error.Client4xxException;
import kea.sem3.jwtdemo.security.dto.SignupRequest;
import kea.sem3.jwtdemo.security.dto.SignupResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SignupResponse createUser(SignupRequest request){

        if(userRepository.existsByUsername(request.getUsername())){
            throw new Client4xxException("Username is taken");
        }
        if(userRepository.existsByEmail(request.getEmail())){
            throw new Client4xxException("Email is used by another user");
        }

        User user = new User(request.getUsername(), request.getEmail(), request.getPassword());


        //All new users are by default given the role CUSTOMER. Comment out the lines below if this is not your required behaviour
        user.addRole(Role.USER);

        userRepository.save(user);
        List<String> roleNames = user.getRoles().stream().map(role -> role.toString()).collect(Collectors.toList());
        //No need to return a body since primary key is the provided userName
        return new SignupResponse(roleNames);

    }

}
