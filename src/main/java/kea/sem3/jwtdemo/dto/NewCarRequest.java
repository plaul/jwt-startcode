package kea.sem3.jwtdemo.dto;

import kea.sem3.jwtdemo.entity.CarBrand;
import kea.sem3.jwtdemo.security.dto.ValidationValues;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter @Setter
@Service
@NoArgsConstructor
@AllArgsConstructor
public class NewCarRequest {
    @Enumerated(EnumType.STRING)
    @NotNull
    CarBrand brand;
    @Size(min = 2,max = 40)
    String model;
    @Positive
    double pricePrDay;
}
