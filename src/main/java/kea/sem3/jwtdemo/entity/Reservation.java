package kea.sem3.jwtdemo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @CreationTimestamp
    private LocalDate reservationDate;


    @Column
    private LocalDate rentalDate;

    @ManyToOne
    Car reservedCar;

    @ManyToOne
    Member reservedTo;

    public Reservation(LocalDate rentalDate, Car reservedCar, Member reservedTo) {
        this.rentalDate = rentalDate;
        this.reservedCar = reservedCar;
        this.reservedTo = reservedTo;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Car getReservedCar() {
        return reservedCar;
    }

    public void setReservedCar(Car reservedCar) {
        this.reservedCar = reservedCar;
    }

    public Member getReservedTo() {
        return reservedTo;
    }

    public void setReservedTo(Member reservedTo) {
        this.reservedTo = reservedTo;
    }

}
