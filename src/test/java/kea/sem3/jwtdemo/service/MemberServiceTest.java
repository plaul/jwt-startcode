package kea.sem3.jwtdemo.service;

import kea.sem3.jwtdemo.dto.MemberDto;
import kea.sem3.jwtdemo.dto.NewMemberRequest;
import kea.sem3.jwtdemo.dto.NewMemberResponse;
import kea.sem3.jwtdemo.entity.Member;
import kea.sem3.jwtdemo.repositories.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @BeforeEach
    public void init(){
        memberService = new MemberService(memberRepository);
    }

    @Test
    void getMembers() {
        Mockito.when(memberRepository.findAll()).thenReturn(List.of(
                new Member("aaa","aaa","aaa","acity","2000", LocalDate.of(2001,12,22),"jw","jw@aa.dk","test12"),
                new Member("aaa","aaa","aaa","acity","2000", LocalDate.of(2001,12,22),"jw","jw@aa.dk","test12")
                ));
        assertEquals(2,memberService.getMembers().size());

    }

    @Test
    void getMemberByUserName() {
        Mockito.when(memberRepository.findByUserName("jw"))
                .thenReturn(java.util.Optional.of(new Member("aaa", "aaa", "aaa", "", "", LocalDate.of(2001, 12, 22), "jw", "jw@aa.dk", "test12")));
        MemberDto mDto = memberService.getMemberByUserName("jw");
        assertEquals("jw",mDto.getUsername());
    }

    //@Test
    void addMember() {
        Member member = new Member("aaa", "aaa", "aaa", "", "", LocalDate.of(2001, 12, 22), "jw", "jw@aa.dk", "test12");
        Mockito.when(memberRepository.save(any(Member.class)))
                .thenReturn(member);
        NewMemberRequest nr = new NewMemberRequest( "jw", "jw@aa.dk", "test12","aaa", "aaa", "aaa", "", "", LocalDate.of(2001, 12, 22));
        //This test is WORHTLESS since we cant test whether the service adds the role
//        NewMemberResponse res = memberService.addMember(nr);
//        assertEquals("USER",res.getRoleNames().get(0));
    }

    @Test
    void editMember() {
    }
}