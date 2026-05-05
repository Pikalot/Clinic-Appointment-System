package termproject.cas.controller;

import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<?> bookAppointment(@RequestBody BookingRequest request) {
        try {
            service.bookAppointment(request);
            return ResponseEntity.status(201).body("Appointment booked successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/{apptId}/cancel")
    public ResponseEntity<?> cancelAppt(@PathVariable Long apptId) {
        try {
            service.cancelAppt(apptId);
            return ResponseEntity.ok("Appointment cancelled");
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(409).body("Conflict! Appointment was modified. Please try again later.");
        }
    }
}
