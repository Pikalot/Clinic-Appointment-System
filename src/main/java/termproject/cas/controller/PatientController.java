package termproject.cas.controller;

import org.springframework.web.bind.annotation.*;
import termproject.cas.model.Appointment;
import termproject.cas.model.BookingRequest;
import termproject.cas.service.AppointmentService;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private final AppointmentService service;

    public PatientController(AppointmentService service) {
        this.service = service;
    }

    @GetMapping("/{mrn}/appointments")
    public List<Appointment> getAppointmentsByMRN(@PathVariable Long mrn) {
        return service.getAllAppointmentsByMRN(mrn);
    }

    @PostMapping("/{mrn}/booking")
    public String bookAppointment(@RequestBody BookingRequest request) {
        service.bookAppointment(request);
        return "Appointment booked and notification sent";
    }
}
