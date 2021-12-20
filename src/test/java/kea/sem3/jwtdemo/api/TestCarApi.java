package kea.sem3.jwtdemo.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import kea.sem3.jwtdemo.dto.CarResponse;
import kea.sem3.jwtdemo.dto.NewCarRequest;
import kea.sem3.jwtdemo.entity.Car;
import kea.sem3.jwtdemo.entity.CarBrand;
import kea.sem3.jwtdemo.repositories.CarRepository;
import kea.sem3.jwtdemo.security.Role;
import kea.sem3.jwtdemo.security.User;
import kea.sem3.jwtdemo.security.UserRepository;
import kea.sem3.jwtdemo.security.dto.LoginRequest;
import kea.sem3.jwtdemo.security.dto.LoginResponse;
import kea.sem3.jwtdemo.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")  //Will prevent the DateSetup CommandlineRunner from running
@SpringBootTest
@AutoConfigureMockMvc
public class TestCarApi {

    @Autowired
    CarRepository carRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String login(String username, String pw) throws Exception {
        MvcResult response = mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new LoginRequest(username, pw))))
                .andReturn();
        return objectMapper.readValue(response.getResponse().getContentAsString(), LoginResponse.class).getToken();
    }

    private Car carVolvo1;
    private Car carVolvo2;
    private Car carFord;

    @BeforeEach
    public void initData() {
        Car[] cars = TestUtils.initThreeCars(carRepository);
        carVolvo1 = cars[0];
        carVolvo2 = cars[1];
        carFord = cars[2];


        User user = new User("admin-for-tests", "a@b.dk", "test12");
        user.addRole(Role.ADMIN);
        userRepository.save(user);
    }

    @Test
    public void testAllCars() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/car")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, CarResponse.class);
        //objectMapper.registerModule(new JavaTimeModule());
        List<CarResponse> res = objectMapper.readValue(response.getResponse().getContentAsString(), typeReference);
        assertThat(res, containsInAnyOrder(
                hasProperty("model", is("V70")),
                hasProperty("model", is("V60")),
                hasProperty("model", is("Kuga"))));
        assertTrue(res.size() == 3);
    }

    @Test
    public void testCarById() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/car/" + carFord.getId())
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        CarResponse res = objectMapper.readValue(response.getResponse().getContentAsString(), CarResponse.class);
        assertEquals(carFord.getId(), res.getId());
    }

    @Test
    public void testCarsByBrand() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/car/by-brand/VOLVO")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, CarResponse.class);
        List<CarResponse> res = objectMapper.readValue(response.getResponse().getContentAsString(), typeReference);
        assertTrue(res.size() == 2);
    }

    @Test
    public void testCarsByNonExistingBrand() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/car/by-brand/VOLVOOOOOO")
                        .accept("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testAddCar() throws Exception {
        String token = TestUtils.login("admin-for-tests", "test12", mockMvc, objectMapper);
        NewCarRequest newCar = new NewCarRequest(CarBrand.WW, "Polo", 200);
        MvcResult response = mockMvc.perform(post("/api/car")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .accept("application/json")
                        .content(objectMapper.writeValueAsString(newCar)))
                .andExpect(status().isCreated())
                .andReturn();
        CarResponse res = objectMapper.readValue(response.getResponse().getContentAsString(), CarResponse.class);
        assertTrue(res.getId() > 0);
    }

    @Test
    //@Transactional
    public void editCar() throws Exception {
        Car ford = carRepository.findById(carFord.getId()).orElse(null);
        System.out.println("FORD ---> "+ ford.getModel());
        System.out.println("----------------------------------------------");


        String token = TestUtils.login("admin-for-tests", "test12", mockMvc, objectMapper);
        NewCarRequest carToEdit = new NewCarRequest(carFord.getBrand(),carFord.getModel(),carFord.getPricePrDay());
        carToEdit.setModel("xxxx");
        carToEdit.setPricePrDay(2);
        mockMvc.perform(put("/api/car/"+carFord.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(carToEdit)))
                .andExpect(status().isOk());
        Car editedCarFromDB = carRepository.findById(carFord.getId()).orElse(null);
        assertEquals("xxxx",editedCarFromDB.getModel());
        assertEquals(2,editedCarFromDB.getPricePrDay());
    }

    @Test
    public void changePrincePrDay() throws Exception {
        String token = TestUtils.login("admin-for-tests", "test12", mockMvc, objectMapper);
        mockMvc.perform(patch("/api/car/"+carFord.getId()+"/2")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        Car editedCarFromDB = carRepository.findById(carFord.getId()).orElse(null);
        assertEquals(2,editedCarFromDB.getPricePrDay());
    }


}