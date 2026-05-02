package termproject.cas.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {
    @GetMapping
    public Map<String, String> health() {
        // Implement this code after integrated to real DB
        // boolean dbOk = checkDatabase();
        boolean dbOk = false; // TO DO: remove this code once DB is integrated
        String dBStatus = dbOk ? "CONNECTED" : "DISCONNECTED";
        boolean notification = checkNotification();
        String notificationStatus = notification ? "CONNECTED" : "DISCONNECTED";

        if (!dbOk) {
            return Map.of(
                    "status", "DOWN",
                    "service", "Clinic Appointment System",
                    "database", dBStatus,
                    "notification", notificationStatus
            );
        }

        return Map.of(
                "status", "UP",
                "service", "Clinic Appointment System",
                "database", dBStatus,
                "notification", notificationStatus
        );
    }

    private boolean checkNotification() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8080/notifications";

            // send a minimal dummy request
            Map<String, Object> testBody = Map.of(
                    "contact", "test@example.com",
                    "provider", "Test",
                    "date", "2026-01-01"
            );

            restTemplate.postForEntity(url, testBody, String.class);

            return true; // service responded
        } catch (Exception e) {
            return false; // service unreachable or failed
        }
    }
}
