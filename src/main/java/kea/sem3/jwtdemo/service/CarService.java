package kea.sem3.jwtdemo.service;

import kea.sem3.jwtdemo.dto.CarResponse;
import kea.sem3.jwtdemo.dto.NewCarRequest;
import kea.sem3.jwtdemo.dto.NewMemberRequest;
import kea.sem3.jwtdemo.entity.Car;
import kea.sem3.jwtdemo.entity.CarBrand;
import kea.sem3.jwtdemo.entity.Member;
import kea.sem3.jwtdemo.error.Client4xxException;
import kea.sem3.jwtdemo.repositories.CarRepository;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<CarResponse> getAllCars(){
        List<Car> cars = carRepository.findAll();
        List<CarResponse> response = cars.stream().map(CarResponse::new).collect(Collectors.toList());
        return response;
    }

    public CarResponse getCarById(int id){
        Car res = carRepository.findById(id).orElseThrow(()->new Client4xxException("Car with provide id not found"));
        return new CarResponse(res);
    }
    public List<CarResponse> getCarsByBrand(CarBrand brand){
        List<Car> cars = carRepository.findCarByBrand(brand);
        List<CarResponse> carResponses= cars.stream().map(CarResponse::new).collect(Collectors.toList());
        return carResponses;
    }

    public CarResponse addCar(@Valid NewCarRequest newCar){
        Car car = carRepository.save(new Car(newCar));
        return new CarResponse(car);
    }

    public void editCar(@Valid NewCarRequest carToEdit, int carId){
        Car car = carRepository.findById(carId).orElseThrow(()-> new Client4xxException("No car with provided ID found"));
        car.setBrand(carToEdit.getBrand());
        car.setModel(carToEdit.getModel());
        car.setPricePrDay(carToEdit.getPricePrDay());
        carRepository.save(car);
    }

    public void updatePrice(int carId,double newPricePrDay){
        Car car = carRepository.getById(carId);
        car.setPricePrDay(newPricePrDay);
        carRepository.save(car);
    }

}
