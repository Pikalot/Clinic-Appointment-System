package termproject.cas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppointmentController {

    @GetMapping("/api/appointments")
    public String home() {
        return "API is running. Try /appointments";
    }
}
