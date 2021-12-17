package kea.sem3.jwtdemo.service;
import kea.sem3.jwtdemo.dto.MemberDto;
import kea.sem3.jwtdemo.dto.NewMemberRequest;
import kea.sem3.jwtdemo.dto.NewMemberResponse;
import kea.sem3.jwtdemo.entity.Member;
import kea.sem3.jwtdemo.error.Client4xxException;
import kea.sem3.jwtdemo.repositories.MemberRepository;
import kea.sem3.jwtdemo.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    MemberRepository memberRepository;


    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberDto> getMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream().map(MemberDto::new).collect(Collectors.toList());
    }

    public MemberDto getMemberByUserName(String username) {
        Member member = memberRepository.findByUserName(username).orElseThrow(() -> new Client4xxException("User not found", HttpStatus.NOT_FOUND));
        return new MemberDto(member);
    }

    public NewMemberResponse addMember(NewMemberRequest memberDto) {

        if (memberRepository.userExist(memberDto.getUsername())) {
            throw new Client4xxException("Provided user name is taken");
        }
        Member member = new Member(memberDto);
        member.addRole(Role.USER);
        member = memberRepository.save(member);
        return new NewMemberResponse(member.getUserName(), member.getDateCreated(), member.getUser().getRoles());
    }

    public void editMember(NewMemberRequest memberToEdit, String userName) {
        if (!memberToEdit.getUsername().equals(userName)) {
            throw new Client4xxException("Cannot change username");
        }
        Member m = memberRepository.findByUserName(userName).orElseThrow(() -> new Client4xxException("User not found"));
        m.setEmail(memberToEdit.getEmail());
        m.setFirstName(memberToEdit.getFirstName());
        if( !(memberToEdit.getPassword() == null) && !memberToEdit.getPassword().isBlank()){
            m.setPassword(memberToEdit.getPassword());
        }
        m.setLastName(memberToEdit.getLastName());
        m.setDateOfBirth(memberToEdit.getDateOfBirth());
        m.setStreet(memberToEdit.getStreet());
        m.setCity(memberToEdit.getCity());
        m.setZip(memberToEdit.getZip());
        memberRepository.save(m);
    }
}

