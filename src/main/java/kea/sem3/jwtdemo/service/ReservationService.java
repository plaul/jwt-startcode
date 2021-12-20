package kea.sem3.jwtdemo.service;

import kea.sem3.jwtdemo.entity.Reservation;
import kea.sem3.jwtdemo.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReservationService {
    ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public boolean isCarFree(int carId, LocalDate date){
        Reservation r = reservationRepository.findReservationByReservedCar_IdAndRentalDate(carId,date);
        return r==null ? true: false;
    }
}
