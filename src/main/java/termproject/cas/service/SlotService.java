package termproject.cas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public List<Slot> getAllAvailableSlots() {
        return slotRepo.findByStatus("Available");
    }

    public Slot getSlotById(Long slotId) {
        return slotRepo.findById(slotId);
    }

    public List<Slot> getAllSlotsByProviderId(Long providerId) {
        return slotRepo.findByProviderId(providerId);
    }

    public Slot addSlot(Slot slot) {
        return slotRepo.save(slot);
    }

    @Transactional
    public boolean cancelSlot(Long slotId) {
        logger.info("Cancel slot request: slotId={}", slotId);

        Slot slot = getSlotById(slotId);
        slot.setStatus("Cancelled");
        boolean res = slotRepo.update(slot);
        if (!res) {
            logger.warn("Update conflict detected: slotId={}", slot.getId());
        }

        logger.info("Slot cancelled successfully: slotId={}", slot.getId());
        return res;
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
