package kea.sem3.jwtdemo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import kea.sem3.jwtdemo.entity.Car;
import kea.sem3.jwtdemo.entity.CarBrand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarResponse {
    Integer id;
    @Enumerated(EnumType.STRING)
    CarBrand brand;
    String model;
    double pricePrDay;

   @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",shape = JsonFormat.Shape.STRING)
    LocalDateTime created;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",shape = JsonFormat.Shape.STRING)
    LocalDateTime updated;

    public CarResponse(Car car) {
        id = car.getId();
        brand = car.getBrand();
        model = car.getModel();
        pricePrDay = car.getPricePrDay();
        created = car.getCreated();
        updated = car.getUpdated();
    }
}
