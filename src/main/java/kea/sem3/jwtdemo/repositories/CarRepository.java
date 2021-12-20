package kea.sem3.jwtdemo.repositories;

import kea.sem3.jwtdemo.entity.Car;
import kea.sem3.jwtdemo.entity.CarBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car,Integer> {

    public List<Car> findCarByBrand(CarBrand brand);

}
