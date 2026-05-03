package termproject.cas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import termproject.cas.model.Provider;
import termproject.cas.repository.ProviderRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProviderService {
    private final ProviderRepository providerRepo;
    private final Logger logger = LoggerFactory.getLogger(ProviderService.class);
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);

    public ProviderService(ProviderRepository providerRepo) {
        this.providerRepo = providerRepo;
    }

    public List<Provider> getAllProviders() {
        return providerRepo.findAll();
    }

    public Optional<Provider> getProviderById(Long providerId) {
        return providerRepo.findById(providerId);
    }

    @Transactional
    public void addProvider(Provider provider) {
        System.out.println("Insert a provider transaction");
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
