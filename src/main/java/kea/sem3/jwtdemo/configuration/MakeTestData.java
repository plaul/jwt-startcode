package kea.sem3.jwtdemo.configuration;

import kea.sem3.jwtdemo.entity.Car;
import kea.sem3.jwtdemo.entity.CarBrand;
import kea.sem3.jwtdemo.entity.Member;
import kea.sem3.jwtdemo.entity.Reservation;
import kea.sem3.jwtdemo.repositories.CarRepository;
import kea.sem3.jwtdemo.repositories.MemberRepository;
import kea.sem3.jwtdemo.repositories.ReservationRepository;
import kea.sem3.jwtdemo.security.Role;
import kea.sem3.jwtdemo.security.User;
import kea.sem3.jwtdemo.security.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

@Controller
@Profile("!test")
public class MakeTestData implements ApplicationRunner {

    MemberRepository memberRepository;
    UserRepository userRepository;
    CarRepository carRepository;
    ReservationRepository reservationRepository;

    public MakeTestData(MemberRepository memberRepository, UserRepository userRepository, CarRepository carRepository, ReservationRepository reservationRepository) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Member m1 = new Member("Kurt","Wonnegut","street","acity","2000",LocalDate.of(2000,10,22),"kw","kw@aa.dk","test12");
        m1.setDateOfBirth(LocalDate.of(2000,10,24));
        m1.addRole(Role.USER);
        Member m2 = new Member("Jannie","Wonnegut","street","acity","2000",LocalDate.of(2001,12,22),"jw","jw@aa.dk","test12");
        m2.addRole(Role.USER);
        memberRepository.save(m1);
        memberRepository.save(m2);

        Car car1 = carRepository.save(new Car(CarBrand.VOLVO,"C40",560));
        carRepository.save(new Car(CarBrand.VOLVO,"V70",500));
        carRepository.save(new Car(CarBrand.VOLVO,"V49",400));
        carRepository.save(new Car(CarBrand.SUZUKI,"Vitara",500));
        carRepository.save(new Car(CarBrand.SUZUKI,"Vitara",500));
        carRepository.save(new Car(CarBrand.SUZUKI,"S-Cross",500));

        Reservation r1 = new Reservation(LocalDate.of(2021, Month.NOVEMBER,4),car1,m1);
        Reservation r2 = new Reservation(LocalDate.of(2021, Month.NOVEMBER,5),car1,m1);
        Reservation r3 = new Reservation(LocalDate.of(2021, Month.NOVEMBER,6),car1,m1);
        Reservation r4 = new Reservation(LocalDate.of(2021, Month.NOVEMBER,7),car1,m1);
        Reservation r5 = new Reservation(LocalDate.of(2021, Month.NOVEMBER,8),car1,m1);
        reservationRepository.save(r1);
        reservationRepository.save(r2);
        reservationRepository.save(r3);
        reservationRepository.save(r4);
        reservationRepository.save(r5);

    }
}
