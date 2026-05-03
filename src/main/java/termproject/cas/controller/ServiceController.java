package termproject.cas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import termproject.cas.model.Service;
import termproject.cas.model.ServiceRequest;
import termproject.cas.service.SerService;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceController {
    private final SerService service;

    public ServiceController(SerService service) {
        this.service = service;
    }

    @GetMapping
    public List<Service> getActiveServices() {
        return service.getActiveServices(true);
    }

    @PostMapping
    public ResponseEntity<?> createService(@RequestBody ServiceRequest request) {
        try {
            service.addService(request);
            return ResponseEntity.status(201).body("Service created");
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(409).body("Conflict! Unable to create service!");
        }
    }

    @PutMapping("/{serviceId}/cancel")
    public ResponseEntity<?> cancelService(@PathVariable int serviceId) {
        try {
            service.cancelService(serviceId);
            return ResponseEntity.ok("Service cancelled");
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(409).body("Conflict! Service was modified. Please refresh.");
        }
    }
}
