package kea.sem3.jwtdemo.service;

import kea.sem3.jwtdemo.dto.CarResponse;
import kea.sem3.jwtdemo.dto.NewCarRequest;
import kea.sem3.jwtdemo.entity.Car;
import kea.sem3.jwtdemo.entity.CarBrand;
import kea.sem3.jwtdemo.repositories.CarRepository;
import kea.sem3.jwtdemo.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CarServiceTest {

    @Autowired
    CarRepository carRepository;

    CarService carService;


    private Car carVolvo1;
    private Car carVolvo2;
    private Car carFord;

    @BeforeEach
    public void initCarData(){
        Car[] cars = TestUtils.initThreeCars(carRepository);
        carVolvo1 = cars[0];
        carVolvo2 = cars[1];
        carFord = cars[2];
        carService = new CarService(carRepository);
    }

    @Test
    void getAllCars() {
        assertEquals(3,carService.getAllCars().size());
    }

    @Test
    void getCarById() {
        CarResponse car = carService.getCarById(carFord.getId());
        assertEquals(carFord.getId(),car.getId());
    }

    @Test
    void getCarsByBrand() {
        List<CarResponse> cars = carService.getCarsByBrand(CarBrand.VOLVO);
        assertEquals(2,cars.size());
    }

    @Test
    void addCar() {
        NewCarRequest newCar = new NewCarRequest(CarBrand.TOYOTA,"Rav-4",900);
        var addedCar = carService.addCar(newCar);
        assertNotNull(addedCar.getId());
        //Verify it got into the DB
        assertNotNull(carRepository.getById(addedCar.getId()));

    }

    @Test
    void editCar() {
        NewCarRequest carToEdit = new NewCarRequest(carFord.getBrand(),carFord.getModel(),carFord.getPricePrDay());
        carToEdit.setModel("xxxx");
        carToEdit.setPricePrDay(2);
        carService.editCar(carToEdit,carFord.getId());
        Car editedCar = carRepository.getById(carFord.getId());
        assertEquals("xxxx",editedCar.getModel());
        assertEquals(2,editedCar.getPricePrDay());
    }

    @Test
    void updatePrice() {
        carService.updatePrice(carFord.getId(),2);
        Car editedCar = carRepository.getById(carFord.getId());
        assertEquals(2,editedCar.getPricePrDay());
    }
}