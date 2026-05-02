package termproject.cas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import termproject.cas.model.Clinic;
import termproject.cas.repository.ClinicRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ClinicService {
    private final ClinicRepository clinicRepo;
    private final Logger logger = LoggerFactory.getLogger(ClinicService.class);
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);

    public ClinicService(ClinicRepository clinicRepo) {
        this.clinicRepo = clinicRepo;
    }

    public List<Clinic> getAllClinics() {
        return clinicRepo.findAll();
    }

    public Clinic getClinicById(Long clinicId) {
        return clinicRepo.findById(clinicId);
    }

    @Transactional
    public void addClinic(Clinic clinic) {
        System.out.println("Insert a clinic transaction");
        successCount.incrementAndGet();
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
