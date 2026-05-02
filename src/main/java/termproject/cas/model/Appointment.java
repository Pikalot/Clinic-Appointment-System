package termproject.cas.model;

public class Appointment {

    private Long id;
    private String status;
    private Slot availableSlot;
    private Patient patient;
    private int serviceId;
    private String service;
    private double fee;
    private int duration;
    private int version;

    public Appointment() {
    }

    public Appointment(
            Long id,
            Slot availSlot,
            Patient patient,
            int version) {
        this.setId(id);
        this.setAvailableSlot(availSlot);
        this.setPatient(patient);
        this.setVersion(version);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Slot getAvailableSlot() {
        return availableSlot;
    }

    public void setAvailableSlot(Slot availableSlot) {
        this.availableSlot = availableSlot;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}
