package kea.sem3.jwtdemo.service;

import kea.sem3.jwtdemo.entity.Car;
import kea.sem3.jwtdemo.entity.CarBrand;
import kea.sem3.jwtdemo.entity.Member;
import kea.sem3.jwtdemo.entity.Reservation;
import kea.sem3.jwtdemo.repositories.CarRepository;
import kea.sem3.jwtdemo.repositories.MemberRepository;
import kea.sem3.jwtdemo.repositories.ReservationRepository;
import kea.sem3.jwtdemo.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ReservationServiceTest {
    @Autowired
    CarRepository carRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ReservationRepository reservationRepository;

    public ReservationService reservationService;

    int carId;
    public void setupTestData(){
        carRepository.deleteAll();
        memberRepository.deleteAll();
        reservationRepository.deleteAll();
        //One car, One Member and one reservation should be enough
        Car car = carRepository.save(new Car(CarBrand.VOLVO,"V40",560));
        carId = car.getId();
        Member member = memberRepository.save(TestUtils.makeMember("userxxx","ux@xx.dk"));
        reservationRepository.save(new Reservation(LocalDate.of(2021, Month.NOVEMBER,4),car,member));
    }

    @BeforeEach
    public void prepareDataForAllTests(){
        setupTestData();
        reservationService = new ReservationService(reservationRepository);
    }

    @Test
    void carIsReserved() {
        boolean carIsFreeOnDate = reservationService.isCarFree(carId,LocalDate.of(2021, Month.NOVEMBER,4));
        assertFalse(carIsFreeOnDate,"Expect false for a car reserved on given date");
    }
    @Test
    void carIsNotReserved() {
        boolean carIsFreeOnDate = reservationService.isCarFree(carId,LocalDate.of(2021, Month.JANUARY,4));
        assertTrue(carIsFreeOnDate,"Expect true for a car NOT reserved on the given date");
    }

}
