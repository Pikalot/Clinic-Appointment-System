package termproject.cas.model;

import java.time.LocalDateTime;

public class Slot {
    private long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Provider provider;

    public Slot() {}

    public Slot(long id, LocalDateTime startTime, LocalDateTime endTime, Provider provider) {
        this.setId(id);
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.setProvider(provider);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
