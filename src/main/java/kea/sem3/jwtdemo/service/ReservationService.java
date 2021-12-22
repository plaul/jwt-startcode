package kea.sem3.jwtdemo.service;

import kea.sem3.jwtdemo.entity.Car;
import kea.sem3.jwtdemo.entity.Member;
import kea.sem3.jwtdemo.entity.Reservation;
import kea.sem3.jwtdemo.error.Client4xxException;
import kea.sem3.jwtdemo.repositories.CarRepository;
import kea.sem3.jwtdemo.repositories.MemberRepository;
import kea.sem3.jwtdemo.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
public class ReservationService {
    ReservationRepository reservationRepository;
    CarRepository carRepository;
    MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository, CarRepository carRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.carRepository = carRepository;
        this.memberRepository = memberRepository;
    }

    public boolean isCarFree(int carId, LocalDate date) {
        System.out.println("-----> "+date);
        Reservation r = reservationRepository.findReservationByReservedCar_IdAndRentalDate(carId, date);
        return r == null ? true : false;
    }

    //@Transactional
    public Reservation reserveCar(int carId, LocalDate date, int memberId) {
        var taken = isCarFree(carId,date);
        if (!isCarFree(carId, date)) {
            throw new Client4xxException("Car is already reserved this day");
        }
        Car car = carRepository.findById(carId).orElseThrow(() -> new Client4xxException("Car with this ID does not exist"));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new Client4xxException("Member with this ID does not exist"));
        Reservation reservation = new Reservation(date, car, member);
        return reservation;  //No DTO here since 1-1 between entity and response
    }
}
