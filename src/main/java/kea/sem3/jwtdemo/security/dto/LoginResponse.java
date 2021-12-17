package kea.sem3.jwtdemo.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {

    private String userName;
    private String token;
    private List<String> roles;

    public LoginResponse(String userName, String token, List<String> roles) {
        this.userName = userName;
        this.token = token;
        this.roles = roles;
    }
}
