package kea.sem3.jwtdemo.repositories;

import kea.sem3.jwtdemo.entity.Member;
import kea.sem3.jwtdemo.security.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        Member m1 = new Member("Kurt","Wonnegut","street","acity","2000",LocalDate.of(2001,12,22),"kw","kw@aa.dk","test12");
        m1.setDateOfBirth(LocalDate.of(2000, 10, 24));
        m1.addRole(Role.USER);
        memberRepository.save(m1);
    }

    @Test
    public void testAddMember(){
        Member member= new Member("Jan","Olsen","street","acity","2000",LocalDate.of(2001,12,22),"jan","jo@aa.dk","test12");;
        assertTrue(member.getId()==0);
        memberRepository.save(member);
        assertTrue(member.getId()>0);
    }
    @Test
    public void testFindByUserName(){
        Member m = memberRepository.findByUserName("kw").orElse(null);
        assertNotNull(m);
        assertEquals("kw",m.getUser().getUsername());
    }

    @Test
    public void testUserExists(){
        boolean exist = memberRepository.userExist("kw");
        assertTrue(exist);
    }
}