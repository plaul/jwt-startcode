package kea.sem3.jwtdemo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kea.sem3.jwtdemo.security.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")  //Will prevent the DateSetup CommandlineRunner from running
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@EnabledIf(expression = "${app.role-test-enabled}",loadContext = true)
class RoleTestingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Test
    void allMsg() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/message/all")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(response.getResponse().getContentAsString().contains("NON-AUTHENTICATED"));
    }

    @Test
    void userMsgNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/message/user")
                        .accept("application/json"))
                .andExpect(status().isUnauthorized());
    }

    /* Testing with WithMockUser
       See this post for why we must use authorities and not roles with this annotation
       https://stackoverflow.com/questions/57324153/how-to-remove-role-prefix-when-testing-spring-security-with-junit
     */
    @Test
    @WithMockUser(username = "kurt", password = "xxxxxx", authorities = "USER")
    void userMsgAuthenticated() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/message/user")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(response.getResponse().getContentAsString().contains("This is a message for USERS"));
    }
    @Test
    @WithMockUser(username = "kurt", password = "xxxxxx", authorities = "ADMIN")
    void userMsgWrongRole() throws Exception {
         mockMvc.perform(get("/api/message/user")
                        .accept("application/json"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "kurt", password = "xxxxxx", authorities = "ADMIN")
    void adminMsgAuthenticated() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/message/admin")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(response.getResponse().getContentAsString().contains("This is a message for ADMINS"));
    }
    @Test
    @WithMockUser(username = "kurt", password = "xxxxxx", authorities = "USER")
    void adminMsgWrongRole() throws Exception {
         mockMvc.perform(get("/api/message/admin")
                        .accept("application/json"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "kurt", password = "xxxxxx", authorities = {"ADMIN","USER"})
    void user_adminMsgBothRoles() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/message/user_admin")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(response.getResponse().getContentAsString().contains("This is a message for USERS and ADMINS"));
    }

    @Test
    @WithMockUser(username = "kurt", password = "xxxxxx", authorities = {"ADMIN"})
    void user_adminOneOfRoles() throws Exception {
        mockMvc.perform(get("/api/message/user_admin")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "kurt", password = "xxxxxx", authorities = {"USER"})
    void user_adminOtherOfRoles() throws Exception {
        mockMvc.perform(get("/api/message/user_admin")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();
    }
}