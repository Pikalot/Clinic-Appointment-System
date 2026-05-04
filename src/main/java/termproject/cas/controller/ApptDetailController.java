package termproject.cas.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import termproject.cas.model.Appointment;
import termproject.cas.service.AppointmentService;

import java.net.http.HttpRequest;
import java.util.Optional;

@Controller
public class ApptDetailController {
    private final AppointmentService service;

    public ApptDetailController(AppointmentService service) {
        this.service = service;
    }

    @GetMapping("/appointment/{id}")
    public String appointment(@PathVariable Long id, Model model) {
        Optional<Appointment> appt = service.getAppointmentById(id);
        // No appointment
        if (appt.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("appt", appt.get());
        model.addAttribute("apptMrn", appt.get().getPatient().getMrn());
        return "appointment";
    }
}
