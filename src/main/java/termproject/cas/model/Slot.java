package termproject.cas.model;

import java.time.LocalDateTime;

public class Slot {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Provider provider;
    private String status;
    private Clinic clinic;
    private int version;

    public Slot() {}

    public Slot(Long id, LocalDateTime startTime, LocalDateTime endTime, Provider provider, String status) {
        this.setId(id);
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.setProvider(provider);
        this.setStatus(status);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }
}
