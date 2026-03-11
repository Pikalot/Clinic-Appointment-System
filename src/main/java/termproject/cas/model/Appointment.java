package termproject.cas.model;

public class Appointment {

    private Long id;
    private String status;
    private Slot availableSlot;
    private Patient patient;
    private String service;
    private double fee;
    private String duration;

    public Appointment() {
    }

    public Appointment(
            Long id,
            Slot availSlot,
            Patient patient) {
        this.setId(id);
        this.setAvailableSlot(availSlot);
        this.setPatient(patient);
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
