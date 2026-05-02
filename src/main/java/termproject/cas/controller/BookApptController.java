package termproject.cas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookApptController {

    @GetMapping("/bookappointment")
    public String bookAppointment() {
        return "apptbooking";
    }
}
