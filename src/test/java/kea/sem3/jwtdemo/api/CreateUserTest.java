package kea.sem3.jwtdemo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kea.sem3.jwtdemo.security.UserRepository;
import kea.sem3.jwtdemo.security.dto.SignupRequest;
import kea.sem3.jwtdemo.security.dto.SignupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")  //Will prevent the DateSetup CommandlineRunner from running
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class CreateUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setupData(){
        SetupTestUsers.addTestUsers(userRepository);
    }

    @Test
    void signUpSuccess() throws Exception {
        SignupRequest signupRequest = new SignupRequest("kurt","k@a.com","test12");
        MvcResult response = mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andReturn();
        SignupResponse signupResponse = objectMapper.readValue(response.getResponse().getContentAsString(), SignupResponse.class);
        assertEquals("USER", signupResponse.getRoles().get(0));
        assertEquals(1,signupResponse.getRoles().size());

        //Verify that new user was actually added to the database
        assertNotNull(userRepository.findByUsername("kurt"));
    }
    @Test
    void signUpIllegalEmail() throws Exception {
        SignupRequest signupRequest = new SignupRequest("kurt","ka.com","test12");
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());

    }
    @Test
    void signUpPasswordToShort() throws Exception {
        SignupRequest signupRequest = new SignupRequest("kurt","k@a.com","test");
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void signUpUserNameToLong() throws Exception {
        SignupRequest signupRequest =
                new SignupRequest("kurtxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx","k@a.com","test12");
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

}
