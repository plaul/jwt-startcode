package kea.sem3.jwtdemo.entity;

import kea.sem3.jwtdemo.dto.NewCarRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Enumerated(EnumType.STRING)
    CarBrand brand;

    String model;

    double pricePrDay;

    @CreationTimestamp
    LocalDateTime created;

    @UpdateTimestamp
    LocalDateTime updated;

    @OneToMany(mappedBy = "reservedCar",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    List<Reservation> reservations = new ArrayList<>();


    public Car(CarBrand brand, String model, double pricePrDay) {
        this.brand = brand;
        this.model = model;
        this.pricePrDay = pricePrDay;
    }
    public Car(NewCarRequest newCar){
        this.brand = newCar.getBrand();
        this.model = newCar.getModel();
        this.pricePrDay = newCar.getPricePrDay();
    }
    public void addReservation(Reservation reservation){
        reservations.add(reservation);
        reservation.setReservedCar(this);
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

}
