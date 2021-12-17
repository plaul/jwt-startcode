package kea.sem3.jwtdemo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kea.sem3.jwtdemo.security.dto.ValidationValues;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
public class NewMemberRequest extends MemberDto {
    @Size(min = ValidationValues.PASSWORD_MIN_SIZE, max = ValidationValues.PASSWORD_MAX_SIZE)
    private String password;

    public NewMemberRequest(String username, String email,  String password,String firstName, String lastName, String street, String city, String zip, LocalDate dateOfBirth) {
        super(username, email, firstName, lastName, street, city, zip, dateOfBirth);
        this.password = password;
    }
}
