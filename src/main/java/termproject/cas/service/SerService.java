package termproject.cas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import termproject.cas.model.ServiceRequest;
import termproject.cas.model.Slot;
import termproject.cas.repository.ServiceRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SerService {
    private final ServiceRepository serviceRepo;
    private final Logger logger = LoggerFactory.getLogger(SerService.class);
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);

    public SerService(ServiceRepository serviceRepo) {
        this.serviceRepo = serviceRepo;
    }

    public List<termproject.cas.model.Service> getAllServices() {
        return serviceRepo.findAll();
    }

    public List<termproject.cas.model.Service> getActiveServices(boolean active) {
        return serviceRepo.findByStatus(active);
    }

    public Optional<termproject.cas.model.Service> getServiceById(int serviceId) {
        return serviceRepo.findById(serviceId);
    }

    @Transactional
    public void addService(ServiceRequest service) {
        System.out.println("Insert a service transaction " + service);
        successCount.incrementAndGet();
    }

    @Transactional
    public void cancelService(int serviceId) {
        logger.info("Cancel service request received: serviceId={}", serviceId);

        Optional<termproject.cas.model.Service> service = getServiceById(serviceId);
        if (service.isEmpty()) {
            logger.warn("Service not found: serviceId={}", serviceId);
            throw new RuntimeException("Service not found: " + serviceId);
        }

        termproject.cas.model.Service serv = service.get();
        serv.setActive(false);
        boolean res = serviceRepo.update(serv);
        if (!res) {
            logger.warn("Update conflict detected: serviceId={}", serv.getServiceId());
        }

        logger.info("Service cancelled successfully: serviceId={}", serv.getServiceId());
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
