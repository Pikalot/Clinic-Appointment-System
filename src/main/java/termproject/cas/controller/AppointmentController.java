package termproject.cas.controller;

import org.springframework.web.bind.annotation.*;
import termproject.cas.model.Appointment;
import termproject.cas.model.BookingRequest;
import termproject.cas.service.AppointmentService;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Appointment> getAppointments() {
        return service.getAllAppointments();
    }

//    @PostMapping
//    public Appointment createAppointment(@RequestBody Appointment appt) {
//        return service.addAppointment(appt);
//    }

    @PostMapping("/booking")
    public String bookAppointment(@RequestBody BookingRequest request) {
        service.bookAppointment(request);
        return "Appointment booked and notification sent";
    }
}
