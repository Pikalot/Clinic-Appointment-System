package termproject.cas.controller;

import org.springframework.web.bind.annotation.*;
import termproject.cas.model.Clinic;
import termproject.cas.service.ClinicService;

import java.util.List;

@RestController
@RequestMapping("/clinics")
public class ClinicController {
    private final ClinicService service;

    public ClinicController(ClinicService service) {
        this.service = service;
    }

    @GetMapping
    public List<Clinic> getAllClinics() {
        return service.getAllClinics();
    }

    @PostMapping
    public void createClinic(@RequestBody Clinic clinic) {
        service.addClinic(clinic);
    }
}
