package kea.sem3.jwtdemo.repositories;

import kea.sem3.jwtdemo.entity.Reservation;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
    Reservation findReservationByReservedCar_IdAndRentalDate(int reservedCar, LocalDate date);
}
