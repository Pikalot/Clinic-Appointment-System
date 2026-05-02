package termproject.cas.controller;

import org.springframework.web.bind.annotation.*;
import termproject.cas.model.Appointment;
import termproject.cas.model.BookingRequest;
import termproject.cas.model.Slot;
import termproject.cas.service.SlotService;

import java.util.List;

@RestController
@RequestMapping("/slots")
public class SlotController {
    private final SlotService service;

    public SlotController(SlotService service) {
        this.service = service;
    }

    @GetMapping
    public List<Slot> getAllSlots() {
        return service.getAllSlots();
    }

    @PostMapping
    public Slot createSlot(@RequestBody Slot slot) {
        return service.addSlot(slot);
    }

//    @PostMapping("/booking")
//    public String bookAppointment(@RequestBody BookingRequest request) {
//        service.bookAppointment(request);
//        return "Appointment booked and notification sent";
//    }
}
