package termproject.cas.model;

public class Appointment {

    private Long id;
    private String status;
    private Slot availableSlot;
    private Service service;
    private Patient patient;

    public Appointment() {
    }

    public Appointment(Long id, String status, Slot availSlot, Service service, Patient patient) {
        this.setId(id);
        this.setStatus(status);
        this.setAvailableSlot(availSlot);
        this.setService(service);
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

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
