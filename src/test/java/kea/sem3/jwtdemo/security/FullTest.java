package kea.sem3.jwtdemo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kea.sem3.jwtdemo.security.dto.LoginRequest;
import kea.sem3.jwtdemo.security.dto.LoginResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")  //Will prevent the DateSetup CommandlineRunner from running
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@EnabledIf(expression = "${app.role-test-enabled}",loadContext = true)
public class FullTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    public static String userJwtToken;
    public static String adminJwtToken;
    public static String user_adminJwtToken;

    @BeforeAll
    public static void loginAndGetTokens(@Autowired ObjectMapper objectMapper,
                                         @Autowired MockMvc mockMvc,
                                         @Autowired UserRepository userRepository) throws Exception {
        SetupTestUsers.addTestUsers(userRepository);
        LoginRequest loginRequest = new LoginRequest("user", "test12");
        MvcResult response = mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn();
        userJwtToken = objectMapper.readValue(response.getResponse().getContentAsString(), LoginResponse.class).getToken();

        loginRequest.setUserName("admin");
        response = mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn();
        adminJwtToken = objectMapper.readValue(response.getResponse().getContentAsString(), LoginResponse.class).getToken(); loginRequest.setUserName("admin");

        loginRequest.setUserName("user_admin");
        response = mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn();
        user_adminJwtToken = objectMapper.readValue(response.getResponse().getContentAsString(), LoginResponse.class).getToken();
    }

    @Test
    void userMsgAuthenticated() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/message/user")
                        .header("Authorization","Bearer "+userJwtToken)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(response.getResponse().getContentAsString().contains("This is a message for USERS"));
    }
    @Test
    void userMsgAuthenticatedWrongRole() throws Exception {
        mockMvc.perform(get("/api/message/user")
                        .header("Authorization","Bearer "+adminJwtToken)
                        .accept("application/json"))
                .andExpect(status().isForbidden());
    }
    @Test
    void adminMsgAuthenticated() throws Exception {
        mockMvc.perform(get("/api/message/admin")
                        .header("Authorization","Bearer "+adminJwtToken)
                        .accept("application/json"))
                .andExpect(status().isOk());
    }
    @Test
    void admiMsgAuthenticatedWrongRole() throws Exception {
        mockMvc.perform(get("/api/message/admin")
                        .header("Authorization","Bearer "+userJwtToken)
                        .accept("application/json"))
                .andExpect(status().isForbidden());
    }

    @Test
    void user_adminMsgAuthenticated() throws Exception {
        mockMvc.perform(get("/api/message/user")
                        .header("Authorization","Bearer "+user_adminJwtToken)
                        .accept("application/json"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/message/admin")
                        .header("Authorization","Bearer "+user_adminJwtToken)
                        .accept("application/json"))
                .andExpect(status().isOk());
    }


}
