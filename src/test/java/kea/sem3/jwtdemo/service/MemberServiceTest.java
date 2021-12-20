package kea.sem3.jwtdemo.service;

import kea.sem3.jwtdemo.dto.MemberDto;
import kea.sem3.jwtdemo.dto.NewMemberRequest;
import kea.sem3.jwtdemo.dto.NewMemberResponse;
import kea.sem3.jwtdemo.entity.Member;
import kea.sem3.jwtdemo.error.Client4xxException;
import kea.sem3.jwtdemo.repositories.MemberRepository;
import kea.sem3.jwtdemo.security.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    MemberService memberService;

    @BeforeEach
    public void initData(){
        Member m1 = new Member("aaa","bbb","street","acity","2000", LocalDate.of(2000,10,22),"aa","aa@aa.dk","test12");
        m1.addRole(Role.USER);
        Member m2 = new Member("bbb","bbb","street","acity","2000", LocalDate.of(2001,12,22),"bb","bb@aa.dk","test12");
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberService = new MemberService(memberRepository);
    }

    @Test
    void getMembers() {
        assertEquals(2,memberService.getMembers().size());
    }

    @Test
    void getMemberByUserName() {
        MemberDto aaMember = memberService.getMemberByUserName("aa");
        assertEquals("aa",aaMember.getUsername());
    }
    @Test
    void throwForNotFoundUser() {
        assertThrows(Client4xxException.class,()-> memberService.getMemberByUserName("dont-exist"));
    }

    @Test
    void addMember() {
        NewMemberRequest newMember = new NewMemberRequest( "cc", "cc@aa.dk", "test12","ccc", "ccc", "ccc", "", "", LocalDate.of(2001, 12, 22));
        NewMemberResponse res = memberService.addMember(newMember);
        assertEquals("USER",res.getRoleNames().get(0));
        assertNotNull(memberRepository.findByUserName("cc").orElse(null));
    }
    @Test
    void addMemberThrowsWhenUserNameTaken() {
        NewMemberRequest newMember = new NewMemberRequest( "aa", "cc@aa.dk", "test12","ccc", "ccc", "ccc", "", "", LocalDate.of(2001, 12, 22));
        assertThrows(Client4xxException.class,()->memberService.addMember(newMember));
    }
    @Test
    void addMemberThrowsWhenEmailTaken() {
        NewMemberRequest newMember = new NewMemberRequest( "cc", "aa@aa.dk", "test12","ccc", "ccc", "ccc", "", "", LocalDate.of(2001, 12, 22));
        assertThrows(Client4xxException.class,()->memberService.addMember(newMember));
    }

    @Test
    void editMember() {
        Member m1 = new Member("aaa","bbb","CHANGED","CHANGED","2000", LocalDate.of(2000,10,22),"aa","aa@aa.dk","test12");
        NewMemberRequest newMember = new NewMemberRequest(m1,"test12");
        memberService.editMember(newMember,"aa");
        Member saved = memberRepository.findByUserName("aa").orElse(null);
        assertEquals("CHANGED",saved.getStreet());
    }

    @Test
    void editMemberThrowsWhenUsernameChanged() {
        Member m1 = new Member("aaa", "bbb", "CHANGED", "CHANGED", "2000", LocalDate.of(2000, 10, 22), "USERNAME_CHANGED", "aa@aa.dk", "test12");
        NewMemberRequest newMember = new NewMemberRequest(m1, "test12");
        assertThrows(Client4xxException.class, () -> memberService.editMember(newMember, "USERNAME_CHANGED"));
    }
    @Test
    void editMemberThrowsWhenNewEmailIsTaken() {
        //bb@aa.dk is already taken
        Member m1 = new Member("aaa", "bbb", "CHANGED", "CHANGED", "2000", LocalDate.of(2000, 10, 22), "aa", "bb@aa.dk", "test12");
        NewMemberRequest newMember = new NewMemberRequest(m1, "test12");
        assertThrows(Client4xxException.class, () -> memberService.editMember(newMember, "aa"));
    }
}