package kea.sem3.jwtdemo.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    @NotBlank
    @Size(min = ValidationValues.USER_NAME_MIN_SIZE, max = ValidationValues.USER_NAME_MAX_SIZE)
    private String username;

    @NotBlank
    @Size(max = ValidationValues.EMAIL_MAX_SIZE)
    @Email
    private String email;

    @NotBlank
    @Size(min = ValidationValues.PASSWORD_MIN_SIZE, max = ValidationValues.PASSWORD_MAX_SIZE)
    private String password;
}
