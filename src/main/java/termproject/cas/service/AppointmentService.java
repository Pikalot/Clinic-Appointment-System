package termproject.cas.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import termproject.cas.assembler.NotificationAssembler;
import termproject.cas.model.*;
import termproject.cas.repository.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.*;

@Service
public class AppointmentService {
    private final AppointmentRepository apptRepo;
    private final SlotRepository slotRepo;
    private final PatientRepository patientRepo;
    private final Logger logger = LoggerFactory.getLogger(AppointmentService.class);
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);

    public AppointmentService(AppointmentRepository apptRepo, SlotRepository slotRepo, PatientRepository patientRepo) {
        this.apptRepo = apptRepo;
        this.slotRepo = slotRepo;
        this.patientRepo = patientRepo;
    }

    public List<Appointment> getAllAppointments() {
        return apptRepo.findAll();
    }

    public List<Appointment> getAllAppointmentsByMRN(Long mRN) {
        return apptRepo.findByMRN(mRN);
    }

//    public Appointment addAppointment(Appointment appt) {
//        return apptRepo.save(appt);
//    }

    @Transactional
    public void bookAppointment(BookingRequest request) {
        long mrn = request.getMrn();
        long slotId = request.getSlotId();

        // Log if successfully receives a request
        logger.info("Booking request received: mrn={}, slotId={}",
                mrn, slotId);

        Patient patient = patientRepo.findById(mrn);
        if (patient == null) {
            failureCount.incrementAndGet();
            logger.warn("Patient not found: mrn={}", mrn);
            throw new RuntimeException("Patient not found");
        }

        Slot slot = slotRepo.findById(slotId);
        if (slot == null) {
            failureCount.incrementAndGet();
            logger.warn("Slot not found: slotId={}", slotId);
            throw new RuntimeException("Slot not found");
        }
        if (slot.getStatus().equals("Taken")) {
            failureCount.incrementAndGet();
            // WARN: slot already taken or booking conflict
            logger.warn("Slot is not available: slotId={}", slotId);
            throw new RuntimeException("Slot is not available");
        }

        // 1. Update slot status taken
        slot.setStatus("Taken");
        boolean updated = slotRepo.update(slot);

        // 2. If another user already changed the slot, reject transaction
        if (!updated) {
            failureCount.incrementAndGet();
            logger.warn("Booking conflict detected: mrn={}, slotId={}", mrn, slotId);
            throw new RuntimeException("Booking conflict detected. Please restart the transaction.");
        }

        // 3. Create a new appointment
        Appointment appt = new Appointment();
        appt.setPatient(patient);
        appt.setAvailableSlot(slot);
        appt.setStatus("SCHEDULED");
        appt.setDuration(1);

        // 4. Insert appointment only if slot update succeeded
        apptRepo.insert(appt);
        successCount.incrementAndGet();
        // INFO: successful booking
        logger.info("Appointment booked successfully: apptId={}, mrn={}, slotId={}",
                appt.getId(),
                mrn,
                slotId);

        // 5. Send a request to Notification Service
        sendNotification(appt);
    }

    private void sendNotification(Appointment appt) {
        long apptId = appt.getId();
        long mrn = appt.getPatient().getMrn();

        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "http://localhost:8080/notifications";
            NotificationRequest body = NotificationAssembler.toRequest(appt);

            logger.info("Calling notification service for: apptId={}, mrn={}",
                    apptId, mrn);

            ResponseEntity<String> response = restTemplate.postForEntity(url, body, String.class);
            logger.info("Notification service responded: apptId={}, status={}",
                    apptId,
                    response.getStatusCode());
        }
        catch (Exception e) {
            logger.error("Notification service failed: apptId={}, mrn={}",
                    apptId, mrn, e);
        }
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
