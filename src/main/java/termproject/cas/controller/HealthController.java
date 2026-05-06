package termproject.cas.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {
    private final JdbcTemplate jdbcTemplate;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public Map<String, String> health() {
        boolean dbOk = checkDatabase();
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

    private boolean checkDatabase() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
