package kea.sem3.jwtdemo.entity;

import kea.sem3.jwtdemo.dto.NewMemberRequest;
import kea.sem3.jwtdemo.security.Role;
import kea.sem3.jwtdemo.security.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private User  user;

    @Column(length = 30,nullable = false)
   // @Min(2) @Max(30)
    private String firstName;

    @Column(length = 50,nullable = false)
    private String lastName;
    private String street;
    private String city;
    private String zip;
    private int ranking;

    private LocalDate dateOfBirth;

    @CreationTimestamp
    LocalDateTime dateCreated;

    @UpdateTimestamp
    LocalDateTime dateUpdate;


    public Member(String firstName, String lastName, String street,String city, String zip, LocalDate dob, String username,String email,String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.dateOfBirth = dob;
        user = new User(username,email,password);
    }
    public Member(NewMemberRequest memberDto) {
        this.firstName = memberDto.getFirstName();
        this.lastName = memberDto.getLastName();
        this.street = memberDto.getStreet();
        this.city = memberDto.getCity();
        this.zip = memberDto.getZip();
        this.dateOfBirth = memberDto.getDateOfBirth();
        user = new User(memberDto.getUsername(),memberDto.getEmail(),memberDto.getPassword());
    }

    public String getUserName(){
        return user.getUsername();
    }
    public void setPassword(String pw){
        user.setPassword(pw);
    }
    public void setEmail(String email){
        user.setPassword(email);
    }
    public String getEmail(){
        return user.getEmail();
    }
    public void addRole(Role role){
        user.addRole(role);
    }
}
