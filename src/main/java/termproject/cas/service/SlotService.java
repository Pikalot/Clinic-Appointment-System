package termproject.cas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import termproject.cas.model.*;
import termproject.cas.repository.SlotRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SlotService {
    private final SlotRepository slotRepo;
    private final Logger logger = LoggerFactory.getLogger(SlotService.class);
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);

    public SlotService(SlotRepository slotRepo) {
        this.slotRepo = slotRepo;
    }

    public List<Slot> getAllSlots() {
        return slotRepo.findAll();
    }

    public List<Slot> getAllSlotsByProviderId(Long providerId) {
        return slotRepo.findByProviderId(providerId);
    }

    public Slot addSlot(Slot slot) {
        return slotRepo.save(slot);
    }


    // Returns the number of failed booking attempts
    public int getFailureCount() {
        return failureCount.get();
    }

    // Returns the number of successful bookings
    public int getSuccessCount() {
        return successCount.get();
    }
}
