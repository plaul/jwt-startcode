package kea.sem3.jwtdemo.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotNull
    private String userName;
    @NotNull
    private String password;
}
