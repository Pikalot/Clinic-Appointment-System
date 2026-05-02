package termproject.cas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import termproject.cas.model.*;
import termproject.cas.repository.ProviderRepository;
import termproject.cas.repository.SlotRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SlotService {
    private final SlotRepository slotRepo;
    private final ProviderRepository providerRepo;
    private final Logger logger = LoggerFactory.getLogger(SlotService.class);
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);

    public SlotService(SlotRepository slotRepo, ProviderRepository providerRepo) {
        this.slotRepo = slotRepo;
        this.providerRepo = providerRepo;
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

    @Transactional
    public void addSlot(SlotRequest request) {
        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime = request.getEndTime();
        Long providerId = request.getProviderId();

        logger.info("Creating slot request received: startTime={}, endTime={}, providerId={}",
            startTime, endTime, providerId);

        // 1. Create a provider
        Provider provider = providerRepo.findById(providerId);
        if (provider == null) {
            failureCount.incrementAndGet();
            logger.warn("Provider not found: providerId={}", providerId);
            throw new RuntimeException("Provider not found");
        }

        // 2. Create a new slot
        Slot slot = new Slot();
        slot.setStartTime(startTime);
        slot.setEndTime(endTime);
        slot.setProvider(provider);

        // 3. Insert slot
        slotRepo.insert(slot);
        successCount.incrementAndGet();
        logger.info("Slot created successfully: startTime={}, endTime={}, providerId={}",
                startTime, endTime, providerId);
    }

    @Transactional
    public void cancelSlot(Long slotId) {
        logger.info("Cancel slot request received: slotId={}", slotId);

        Slot slot = getSlotById(slotId);
        slot.setStatus("Cancelled");
        boolean res = slotRepo.update(slot);
        if (!res) {
            logger.warn("Update conflict detected: slotId={}", slot.getId());
        }

        logger.info("Slot cancelled successfully: slotId={}", slot.getId());
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
