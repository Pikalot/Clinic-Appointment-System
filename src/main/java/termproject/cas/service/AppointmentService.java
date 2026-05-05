package termproject.cas.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import termproject.cas.assembler.NotificationAssembler;
import termproject.cas.model.*;
import termproject.cas.repository.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.*;

@Service
public class AppointmentService {
    private final AppointmentRepository apptRepo;
    private final SlotRepository slotRepo;
    private final PatientRepository patientRepo;
    private final Logger logger = LoggerFactory.getLogger(AppointmentService.class);
    private final AtomicInteger failureCreateCount = new AtomicInteger(0);
    private final AtomicInteger successBookingCount = new AtomicInteger(0);
    private final AtomicInteger failureCancelCount = new AtomicInteger(0);
    private final AtomicInteger successCancelCount = new AtomicInteger(0);

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

    public Optional<Appointment> getAppointmentById(Long apptId) {
        return apptRepo.findById(apptId);
    }

    @Transactional
    public ResponseEntity<?> cancelAppt(@PathVariable Long apptId) {
        // Log if successfully receives a request
        logger.info("Appointment cancel request received: apptId={}", apptId);

        // 1. Find the appointment
        Optional<Appointment> appt = apptRepo.findById(apptId);
        if (appt.isEmpty()) {
            failureCancelCount.incrementAndGet();
            logger.warn("Appointment not found: apptId={}", apptId);
            throw new RuntimeException("Appointment not found");
        }

        // 2. Update appointment status to cancel
        Appointment appointment = appt.get();
        if (!appointment.getStatus().equals("Scheduled")) {
            failureCancelCount.incrementAndGet();
            logger.warn("Appointment is not scheduled: apptId={}", apptId);
            throw new RuntimeException("Appointment is not scheduled");
        }

        appointment.setStatus("Cancelled");
        boolean updatedAppt = apptRepo.update(appointment);
        if (!updatedAppt) {
            failureCancelCount.incrementAndGet();
            logger.warn("Updating conflict detected: apptId={}", apptId);
            throw new RuntimeException("Updating conflict detected. Please restart the transaction.");
        }

        // 3. Update slot status to available
        Long slotId = appointment.getAvailableSlot().getId();
        Optional<Slot> freshSlot = slotRepo.findById(slotId);
        if (freshSlot.isEmpty()) {
            failureCancelCount.incrementAndGet();
            logger.warn("No valid slot in the appointment: apptId={}", apptId);
            throw new RuntimeException("No valid slot in the appointment");
        }

        Slot slot = freshSlot.get();
        slot.setStatus("Available");
        boolean updatedSlot = slotRepo.update(slot);
        if (!updatedSlot) {
            failureCancelCount.incrementAndGet();
            logger.warn("Updating slot conflict detected: apptId={}, slotId={}", appt, slot.getId());
            throw new RuntimeException("Updating slot conflict detected. Please restart the transaction.");
        }

        successCancelCount.incrementAndGet();
        // INFO: successful booking
        logger.info("Appointment cancelled successfully: apptId={}, slotId={}",
                apptId,
                slot.getId());

        return ResponseEntity.ok("Appointment cancelled");
    }

    @Transactional
    public void bookAppointment(BookingRequest request) {
        long mrn = request.getMrn();
        long slotId = request.getSlotId();
        int serviceId = request.getServiceId();
        LocalDateTime currentTime = LocalDateTime.now();

        // Log if successfully receives a request
        logger.info("Booking request received: mrn={}, slotId={}",
                mrn, slotId);

        Optional<Patient> patient = patientRepo.findByMRN(mrn);
        if (patient.isEmpty()) {
            failureCreateCount.incrementAndGet();
            logger.warn("Patient not found: mrn={}, slotId={}", mrn, slotId);
            throw new RuntimeException("Patient not found");
        }

        Optional<Slot> slot = slotRepo.findById(slotId);
        if (slot.isEmpty()) {
            failureCreateCount.incrementAndGet();
            logger.warn("Slot not found: mrn={}, slotId={}", mrn, slotId);
            throw new RuntimeException("Slot not found");
        }

        Slot sl = slot.get();
        if (sl.getStatus().equals("Taken")) {
            failureCreateCount.incrementAndGet();
            // WARN: slot already taken or booking conflict
            logger.warn("Slot is not available: mrn={}, slotId={}", mrn, slotId);
            throw new RuntimeException("Slot is not available");
        }
        if (sl.getStartTime().isBefore(currentTime)) {
            failureCreateCount.incrementAndGet();
            // WARN: slot is stale
            logger.warn("Slot is expired: mrn={}, slotID={}, startDate={}, currentTime={}",
                mrn, slotId, slot.get().getStartTime(), currentTime);
            throw new RuntimeException("Slot is expired");
        }

        // 1. Update slot status taken
        sl.setStatus("Taken");
        boolean updated = slotRepo.update(sl);

        // 2. If another user already changed the slot, reject transaction
        if (!updated) {
            failureCreateCount.incrementAndGet();
            logger.warn("Booking conflict detected: mrn={}, slotId={}", mrn, slotId);
            throw new RuntimeException("Booking conflict detected. Please restart the transaction.");
        }

        // 3. Create a new appointment
        Appointment appt = new Appointment();
        appt.setPatient(patient.get());
        appt.setAvailableSlot(sl);
        appt.setStatus("Scheduled");
        appt.setDuration(1);
        appt.setServiceId(serviceId);

        // 4. Insert appointment only if slot update succeeded
        boolean success = apptRepo.insert(appt);
        if (!success) {
            failureCreateCount.incrementAndGet();
            logger.warn("Writing appointment to database failed: mrn={}, slotId={}",
                    mrn, slotId);
        }
        successBookingCount.incrementAndGet();
        // INFO: successful booking
        logger.info("Appointment booked successfully: mrn={}, slotId={}",
                mrn,
                slotId);

        // 5. Send a request to Notification Service
        // -> Check Logs and Console for external service outcomes
        sendNotification(appt);
    }

    private void sendNotification(Appointment appt) {
        Long mrn = appt.getPatient().getMrn();
        Long slotId = appt.getAvailableSlot().getId();

        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "http://localhost:8080/notifications";
            NotificationRequest body = NotificationAssembler.toRequest(appt);

            System.out.println("Calling notification service for: " + appt.getPatient().getEmails().get(0));
            logger.info("Calling notification service for: mrn={}, slotId={}",
                    mrn, slotId);

            ResponseEntity<String> response = restTemplate.postForEntity(url, body, String.class);

            System.out.println("Notification service responded with: " + response.getBody());
            logger.info("Notification service responded: mrn={}, slotId={}, status={}",
                    mrn,
                    slotId,
                    response.getStatusCode());
        }
        catch (Exception e) {
            logger.error("Notification service failed: mrn={}, slotId={}",
                    mrn, slotId, e);
        }
    }

    // Returns the number of failed booking attempts
    public int getFailureCreateCount() {
        return failureCreateCount.get();
    }

    // Returns the number of successful bookings
    public int getSuccessBookingCount() {
        return successBookingCount.get();
    }
}
