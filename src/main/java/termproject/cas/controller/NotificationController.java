package termproject.cas.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @PostMapping
    public ResponseEntity<Map<String, Object>> sendNotification(@RequestBody Map<String, Object> request) {
        System.out.println("Notification received: " + request);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "SENT");
        response.put("recipient", request.get("contact"));
        response.put("message", "Appointment confirmed with Dr. " + request.get("provider")
                + " on " + request.get("date"));
        response.put("notificationId", UUID.randomUUID().toString());
        response.put("timestamp", java.time.Instant.now().toString());

        return ResponseEntity.ok(response);
    }
}
