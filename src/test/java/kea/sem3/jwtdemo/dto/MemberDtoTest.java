package kea.sem3.jwtdemo.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

class MemberDtoTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getDateOfBirth() {
    }

    @Test
    void setDateOfBirth() throws JsonProcessingException {
        String memberDtoJson = "{\n" +
                "  \"username\": \"kwfasf\",\n" +
                "  \"email\": \"kwdf@aa.dk\",\n" +
                "  \"firstName\": \"Kurt\",\n" +
                "  \"lastName\": \"Wonnegut\",\n" +
                "  \"dateOfBirth\": \"24-10-2000\",\n" +
                "  \"street\" : \"Lynby vej\",\n" +
                "  \"city\" : \"Lyngby\",\n" +
                "  \"zip\": \"2800\"\n" +
                "\n" +
                "}";
        assertNotNull(objectMapper);
        objectMapper.registerModule(new JavaTimeModule());
        MemberDto memberDto = objectMapper.readValue(memberDtoJson,MemberDto.class);

    }
}