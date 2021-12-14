package kea.sem3.jwtdemo.security.api;

import kea.sem3.jwtdemo.error.Client4xxException;
import kea.sem3.jwtdemo.security.UserService;
import kea.sem3.jwtdemo.security.dto.LoginRequest;
import kea.sem3.jwtdemo.security.dto.LoginResponse;
import kea.sem3.jwtdemo.security.dto.SignupRequest;
import kea.sem3.jwtdemo.security.dto.SignupResponse;
import kea.sem3.jwtdemo.security.jwt.JwtTokenUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthenticationApi {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public AuthenticationApi(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
            UserDetails user = (UserDetails) authenticate.getPrincipal();
            String token = jwtTokenUtil.generateAccessToken(user);
            List<String> rolesNames = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(new LoginResponse(user.getUsername(), token, rolesNames));
        } catch( org.springframework.security.core.AuthenticationException ex) {
            throw new Client4xxException("Incorrect username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("register")
    public ResponseEntity<SignupResponse> register(@RequestBody @Valid SignupRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

}
