package kea.sem3.jwtdemo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import kea.sem3.jwtdemo.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDto {
    String username;
    String email;
    String firstName;
    String lastName;
    String street;
    String city;
    String zip;
    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    LocalDate dateOfBirth;

    public MemberDto(Member m) {
        this.username = m.getUser().getUsername();
        this.email = m.getUser().getEmail();
        this.firstName = m.getFirstName();
        this.lastName = m.getLastName();
        this.street = m.getStreet();
        this.city = m.getCity();
        this.zip = m.getZip();
        this.dateOfBirth = m.getDateOfBirth();
    }
}
