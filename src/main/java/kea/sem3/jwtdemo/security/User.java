package kea.sem3.jwtdemo.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class User {

   public static final PasswordEncoder pwEncoder = new BCryptPasswordEncoder();

   @Id
   private String username;

   @Email
   @Column(nullable = false, unique = true,length = 50)
   private String email;

   // 72 == Max length of a bcrypt encoded password
   // https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html
   @Column(nullable = false, length = 72)
   private String password;

   boolean enabled;

   @Enumerated(EnumType.STRING)
   @Column(columnDefinition = "ENUM('USER','ADMIN')")
   @ElementCollection(fetch = FetchType.EAGER)
   List<Role> roles = new ArrayList<>();

   public User() {
   }

   public User(String username, String email, String password) {
      this.username = username;
      this.email = email;
      this.password = pwEncoder.encode(password);
      this.enabled = true;
   }

   public void setPassword(String password) {
      this.password = pwEncoder.encode(password);
   }

   public List<Role> getRoles() {
      return roles;
   }
   public void addRole(Role role){
      roles.add(role);
   }

}
