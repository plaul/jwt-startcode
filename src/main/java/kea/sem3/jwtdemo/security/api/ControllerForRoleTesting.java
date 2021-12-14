package kea.sem3.jwtdemo.security.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;

/*API Only used to test the Role-based features of security */

@AllArgsConstructor
@NoArgsConstructor
@Getter
class TestResponse {
    String msg;
    String pricipal;
}

@RestController
@ConditionalOnExpression("${app.role-test-enabled}")
@RequestMapping("/api/message")
public class ControllerForRoleTesting {

    @GetMapping("all")
    public ResponseEntity<?> allMsg() {
        return ResponseEntity.ok(new TestResponse("This is a message for  (NON-AUTHENTICATED)","anonymous"));

    }

    @RolesAllowed("USER")
    @GetMapping("user")
    public ResponseEntity<?> userMsg(Principal principal) {
        return ResponseEntity.ok(new TestResponse("This is a message for USERS)", principal.getName()));
    }

    @RolesAllowed("ADMIN")
    @GetMapping("admin")
    public ResponseEntity<?> adminMsg(Principal principal) {
        return ResponseEntity.ok(new TestResponse("This is a message for ADMINS)", principal.getName()));
    }

    @RolesAllowed({"USER", "ADMIN"})
    @GetMapping("user_admin")
    public ResponseEntity<?> admin_userMsg(Principal principal) {
        return ResponseEntity.ok(new TestResponse("This is a message for USERS and ADMINS)", principal.getName()));
    }
}


