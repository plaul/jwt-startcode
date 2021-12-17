package kea.sem3.jwtdemo.api;

import kea.sem3.jwtdemo.dto.MemberDto;
import kea.sem3.jwtdemo.dto.NewMemberRequest;
import kea.sem3.jwtdemo.dto.NewMemberResponse;
import kea.sem3.jwtdemo.error.Client4xxException;
import kea.sem3.jwtdemo.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/member")

public class MemberController {

    MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<MemberDto>> getAllMembers(){
        return ResponseEntity.ok(memberService.getMembers());
    }

    @GetMapping("authenticated-user")
    @RolesAllowed("USER")
    public ResponseEntity<MemberDto> getAuthenticatedUser(Principal principal){
        System.out.println(principal.getName());
        return ResponseEntity.ok(memberService.getMemberByUserName(principal.getName()));
    }

    @GetMapping("/{username}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<MemberDto> getUserFromUserName(@PathVariable String username){
        return ResponseEntity.ok(memberService.getMemberByUserName(username));
    }

    @PostMapping
    public ResponseEntity<NewMemberResponse> addMember(@Valid @RequestBody NewMemberRequest newMember){
        NewMemberResponse response = memberService.addMember(newMember);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RolesAllowed("USER")
    @PutMapping()
    public ResponseEntity editYourSelf(@Valid @RequestBody NewMemberRequest memberToEdit,Principal principal){
        memberService.editMember(memberToEdit,principal.getName());
        return ResponseEntity.ok().build();
    }


}
