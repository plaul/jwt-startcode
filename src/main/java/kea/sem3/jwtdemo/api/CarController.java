package kea.sem3.jwtdemo.api;


import kea.sem3.jwtdemo.dto.CarResponse;
import kea.sem3.jwtdemo.dto.NewCarRequest;
import kea.sem3.jwtdemo.entity.Car;
import kea.sem3.jwtdemo.entity.CarBrand;
import kea.sem3.jwtdemo.error.Client4xxException;
import kea.sem3.jwtdemo.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("api/car")
public class CarController {

    CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<CarResponse> allCars(){
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    public CarResponse carById(@PathVariable  int id){
        return carService.getCarById(id);
    }

    @GetMapping("/by-brand/{brand}")
    public List<CarResponse> carByBrand(@PathVariable  String brand){
        try {
            CarBrand carBrand = CarBrand.valueOf(brand.toUpperCase());
            return carService.getCarsByBrand(carBrand);
        } catch (IllegalArgumentException e){
            throw new Client4xxException("Car brand not recognized");
        }
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<CarResponse> addCar(@RequestBody NewCarRequest newCar){
        CarResponse res = carService.addCar(newCar);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity editCar(@RequestBody NewCarRequest newCar,@PathVariable Integer id){
        carService.editCar(newCar,id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/{new_price}")
    @RolesAllowed("ADMIN")
    public ResponseEntity editPricePrDay(@PathVariable Integer id, @PathVariable Double new_price){
        carService.updatePrice(id,new_price);
        return ResponseEntity.ok().build();
    }


}
