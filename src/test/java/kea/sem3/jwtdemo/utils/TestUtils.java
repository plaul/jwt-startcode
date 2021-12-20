package kea.sem3.jwtdemo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import kea.sem3.jwtdemo.entity.Car;
import kea.sem3.jwtdemo.entity.CarBrand;
import kea.sem3.jwtdemo.entity.Member;
import kea.sem3.jwtdemo.repositories.CarRepository;
import kea.sem3.jwtdemo.security.Role;
import kea.sem3.jwtdemo.security.User;
import kea.sem3.jwtdemo.security.UserRepository;
import kea.sem3.jwtdemo.security.dto.LoginRequest;
import kea.sem3.jwtdemo.security.dto.LoginResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class TestUtils {

    public static Car[] initThreeCars(CarRepository carRepository) {
        carRepository.deleteAll();
        Car[] cars = new Car[3];
        cars[0] = new Car(CarBrand.VOLVO, "V70", 700);
        cars[1] = new Car(CarBrand.VOLVO, "V60", 600);
        cars[2] = new Car(CarBrand.FORD, "Kuga", 900);
        carRepository.save(cars[0]);
        carRepository.save(cars[1]);
        carRepository.save(cars[2]);
        return cars;
    }

    public static Member makeMember(String username, String email){
        return  new Member("aaa","bbb","street","acity","2000", LocalDate.of(2000,10,22),username,email,"test12");
    }

    public static String login(String username, String pw, MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        MvcResult response = mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new LoginRequest(username, pw))))
                .andReturn();
        return objectMapper.readValue(response.getResponse().getContentAsString(), LoginResponse.class).getToken();
    }
}
