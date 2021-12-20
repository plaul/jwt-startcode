package kea.sem3.jwtdemo.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import kea.sem3.jwtdemo.dto.MemberDto;
import kea.sem3.jwtdemo.dto.NewMemberRequest;
import kea.sem3.jwtdemo.dto.NewMemberResponse;
import kea.sem3.jwtdemo.entity.Member;
import kea.sem3.jwtdemo.repositories.MemberRepository;
import kea.sem3.jwtdemo.security.Role;
import kea.sem3.jwtdemo.security.User;
import kea.sem3.jwtdemo.security.UserRepository;
import kea.sem3.jwtdemo.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.Matchers.*;

//import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")  //Will prevent the DateSetup CommandlineRunner from running
@SpringBootTest
@AutoConfigureMockMvc
//@AutoConfigureTestDatabase
public class TestMemberApi {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setupUsers() {
        memberRepository.deleteAll();
        userRepository.deleteAll();
        Member m1 = new Member("Kurt","Wonnegut","street","acity","2000",LocalDate.of(2001,12,22),"kw","kw@aa.dk","test12");
        m1.setDateOfBirth(LocalDate.of(2000, 10, 24));
        m1.addRole(Role.USER);
        Member m2 = new Member("Jannie","Wonnegut","street","acity","2000",LocalDate.of(2001,12,22),"jw","jw@aa.dk","test12");;
        m2.addRole(Role.USER);
        memberRepository.save(m1);
        memberRepository.save(m2);
        User user = new User("admin-for-tests", "a@b.dk", "test12");
        user.addRole(Role.ADMIN);
        userRepository.save(user);
    }

    @Test
    @WithMockUser(username = "xxx", password = "xxxxxx", authorities = "ADMIN")
    public void testAllMembers() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/member")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, MemberDto.class);
        List<MemberDto> res = objectMapper.readValue(response.getResponse().getContentAsString(), typeReference);
        assertThat(res, containsInAnyOrder(hasProperty("username", is("jw")), hasProperty("username", is("kw"))));
        assertTrue(res.size() == 2);
    }

     @Test
    public void testGetAuthenticatedUser() throws Exception {
        String token = TestUtils.login("kw", "test12",mockMvc,objectMapper);
        MvcResult response = mockMvc.perform(get("/api/member/authenticated-user")
                        .header("Authorization", "Bearer " + token)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        MemberDto res = objectMapper.readValue(response.getResponse().getContentAsString(), MemberDto.class);
        assertEquals("kw", res.getUsername());
    }

    @Test
    public void testGetUserFromUserName() throws Exception {
        String token = TestUtils.login("admin-for-tests", "test12",mockMvc,objectMapper);
        MvcResult response = mockMvc.perform(get("/api/member/kw")
                        .header("Authorization", "Bearer " + token)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        MemberDto res = objectMapper.readValue(response.getResponse().getContentAsString(), MemberDto.class);
        assertEquals("kw", res.getUsername());
    }

    @Test
    void testAddMember() throws Exception {
        NewMemberRequest m = new NewMemberRequest("user-1", "user-1@a.dk", "test12", "Jan", "Olsen", "Lyngbyvej 21", "Lyngby", "2800", LocalDate.of(2000, 12, 22));

        MvcResult response = mockMvc.perform(post("/api/member")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(m)))
                .andExpect(status().isCreated())
                .andReturn();
        NewMemberResponse res = objectMapper.readValue(response.getResponse().getContentAsString(),NewMemberResponse.class);
        assertEquals("USER",res.getRoleNames().get(0));
    }
    @Test
    void testAddMemberUserNameTaken() throws Exception {
        NewMemberRequest m = new NewMemberRequest("kw", "user-1@a.dk", "test12", "Jan", "Olsen", "Lyngbyvej 21", "Lyngby", "2800", LocalDate.of(2000, 12, 22));

        mockMvc.perform(post("/api/member")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(m)))
                .andExpect(status().isBadRequest());

    }
}






