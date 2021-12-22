package kea.sem3.jwtdemo.api;

import kea.sem3.jwtdemo.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("api/reservation")
public class ReservationController {

    ReservationService reservationService;
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/free/{carId}/{date}")
    boolean isCarFree(@PathVariable int carId, @PathVariable String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
        LocalDate d = LocalDate.parse(date,formatter);
        return reservationService.isCarFree(carId,d);
    }

    @PostMapping
    @RolesAllowed("USER")
    public void makeReservation(@PathVariable int carId, @PathVariable String date, Principal principal){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
        LocalDate d = LocalDate.parse(date,formatter);
        //reservationService.reserveCar(carId,date,principal.getName());


    }
}
