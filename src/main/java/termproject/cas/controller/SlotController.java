package termproject.cas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import termproject.cas.model.Slot;
import termproject.cas.model.SlotRequest;
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
    public List<Slot> getAllAvailableSlots() {
        return service.getAllAvailableSlots();
    }

    @PostMapping
    public ResponseEntity<?> createSlot(@RequestBody SlotRequest request) {
        try {
            service.addSlot(request);
            return ResponseEntity.status(201).body("Slot created");
        }
        catch (RuntimeException e) {
            String msg = switch (e.getMessage()) {
                case "Time conflict found" -> "This time slot overlaps with an existing slot.";
                case "Slot not found" -> "Slot no longer exists.";
                default -> "Unable to create slot. Please try again.";
            };
            return ResponseEntity.status(409).body(msg);
        }
    }

    @PutMapping("/{slotId}/cancel")
    public ResponseEntity<?> cancelSlot(@PathVariable Long slotId) {
        try {
            service.cancelSlot(slotId);
            return ResponseEntity.ok("Slot cancelled");
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(409).body("Conflict! Slot was modified. Please refresh.");
        }
    }
}
