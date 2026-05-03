package termproject.cas.model;

public class Service {
    private int serviceId;
    private String name;
    private double fee;
    private int duration;
    private int version;
    private boolean isActive;

    public Service() {}

    public Service(int serviceId, String name, double fee, int duration) {
        this.setServiceId(serviceId);
        this.setName(name);
        this.setFee(fee);
        this.setDuration(duration);
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }
}
