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
import java.util.Optional;
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

    public Optional<Slot> getSlotById(Long slotId) {
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

        // 1. Check for time conflict
        if (slotRepo.hasOverlap(providerId, startTime, endTime)) {
            failureCount.incrementAndGet();
            logger.warn("Time conflict found: startTime={}, endTime={}, providerId={}",
                startTime, endTime, providerId);
            throw new RuntimeException("Time conflict found");
        }

        // 2. Create a provider
        Optional<Provider> provider = providerRepo.findById(providerId);
        if (provider.isEmpty()) {
            failureCount.incrementAndGet();
            logger.warn("Provider not found: startTime={}, endTime={}, providerId={}",
                    startTime, endTime, providerId);
            throw new RuntimeException("Provider not found");
        }

        // 3. Create a new slot
        Slot slot = new Slot();
        slot.setStartTime(startTime);
        slot.setEndTime(endTime);
        slot.setProvider(provider.get());

        // 4. Insert slot
        slotRepo.insert(slot);
        successCount.incrementAndGet();
        logger.info("Slot created successfully: startTime={}, endTime={}, providerId={}",
                startTime, endTime, providerId);
    }

    @Transactional
    public void cancelSlot(Long slotId) {
        logger.info("Cancel slot request received: slotId={}", slotId);

        Optional<Slot> slot = getSlotById(slotId);
        if (slot.isEmpty()) {
            logger.warn("Slot not found: slotId={}", slotId);
            throw new RuntimeException("Slot not found");
        }

        Slot sl = slot.get();
        sl.setStatus("Cancelled");
        boolean res = slotRepo.update(sl);
        if (!res) {
            logger.warn("Update conflict detected: slotId={}", slotId);
        }

        logger.info("Slot cancelled successfully: slotId={}", slotId);
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
