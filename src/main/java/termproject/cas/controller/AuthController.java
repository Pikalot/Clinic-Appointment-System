package termproject.cas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import termproject.cas.model.LoginRequest;
import termproject.cas.model.Patient;
import termproject.cas.model.User;
import termproject.cas.repository.PatientRepository;
import termproject.cas.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PatientRepository patientRepo;
    private final UserRepository userRepo;

    public AuthController(PatientRepository patientRepo, UserRepository userRepo) {
        this.patientRepo = patientRepo;
        this.userRepo = userRepo;
    }

    // Endpoint 1: Patient login
    @PostMapping("/login/patient")
    public ResponseEntity<?> loginPatient(@RequestBody LoginRequest request) {
        // Search in Patients table only
        Optional<Patient> patient = patientRepo.findByUsernameAndPassword(request.getUsername(), request.getPassword());
        System.out.println("log in patient" + patient.get().getFirstName());
        if (patient.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        Map<String, Object> res = new HashMap<>();
        res.put("mrn", patient.get().getMrn());
        res.put("role", "Patient");
        return ResponseEntity.ok(res);
    }

    // Endpoint 2: Staff login
    @PostMapping("/login/staff")
    public ResponseEntity<?> loginStaff(@RequestBody LoginRequest request) {
        // Search in Users table only
        Optional<User> user = userRepo.findByUsernameAndPassword(request.getUsername(), request.getPassword());

        if (user.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        Long id = user.get().getId();
        String role = userRepo.findRoleById(id);

        Map<String, Object> res = new HashMap<>();
        res.put("userId", id);
        res.put("role", role);
        return ResponseEntity.ok(res);
    }
}
