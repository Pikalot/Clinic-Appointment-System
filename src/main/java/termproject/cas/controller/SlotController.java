package termproject.cas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import termproject.cas.model.Slot;
import termproject.cas.service.SlotService;

import java.util.List;
import java.util.Map;

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
    public Slot createSlot(@RequestBody Slot slot) {
        return service.addSlot(slot);
    }

    @PutMapping("/{slotId}/cancel")
    public ResponseEntity<?> cancelSlot(@PathVariable Long slotId) {
        boolean success = service.cancelSlot(slotId);

        if (!success) {
            return ResponseEntity.status(409).body("Conflict! Slot was modified. Please refresh.");
        }
        return ResponseEntity.ok("Slot cancelled");
    }
}
