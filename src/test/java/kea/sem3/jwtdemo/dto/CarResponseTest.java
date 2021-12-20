package kea.sem3.jwtdemo.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CarResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();



    @Test
    void xxx() throws JsonProcessingException {
        String json = "{\n" +
                "  \"id\" : 1,\n" +
                "  \"brand\" : \"VOLVO\",\n" +
                "  \"model\" : \"V70\",\n" +
                "  \"pricePrDay\" : 500.0,\n" +
                "  \"created\" : \"19-12-2021 17:20:21\",\n" +
                "  \"updated\" : \"19-12-2021 17:20:21\"\n" +
                "}";
        assertNotNull(objectMapper);
        objectMapper.registerModule(new JavaTimeModule());
        CarResponse res = objectMapper.readValue(json,CarResponse.class);

    }
}