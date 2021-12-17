package kea.sem3.jwtdemo.configuration;

import kea.sem3.jwtdemo.entity.Member;
import kea.sem3.jwtdemo.repositories.MemberRepository;
import kea.sem3.jwtdemo.security.Role;
import kea.sem3.jwtdemo.security.User;
import kea.sem3.jwtdemo.security.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@Profile("!test")
public class MakeTestData implements ApplicationRunner {

    MemberRepository memberRepository;
    UserRepository userRepository;

    public MakeTestData(MemberRepository memberRepository, UserRepository userRepository) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        memberRepository.deleteAll();
        userRepository.deleteAll();
        Member m1 = new Member("Kurt","Wonnegut","street","acity","2000",LocalDate.of(2000,10,22),"kw","kw@aa.dk","test12");
        m1.setDateOfBirth(LocalDate.of(2000,10,24));
        m1.addRole(Role.USER);
        Member m2 = new Member("Jannie","Wonnegut","street","acity","2000",LocalDate.of(2001,12,22),"jw","jw@aa.dk","test12");
        m2.addRole(Role.USER);
        memberRepository.save(m1);
        memberRepository.save(m2);
    }
}
