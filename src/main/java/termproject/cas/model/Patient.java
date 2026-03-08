package termproject.cas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.List;

public class Patient {
    private long mrn;
    private String firstName;
    private String middleName;
    private String lastName;
    private String sex;
    private LocalDate dob;
    private EmergencyContact emergencyContact;
    private String relationship;
    @JsonIgnore
    private List<Appointment> appointments;

    public Patient() {}

    public Patient(long mrn, String fn, String mid, String ln, String sex, LocalDate dob, EmergencyContact contact, String relationship, List<Appointment> appointments) {
        this.setMrn(mrn);
        this.setFirstName(fn);
        this.setLastName(ln);
        this.setMiddleName(mid);
        this.setSex(sex);
        this.setDob(dob);
        this.setEmergencyContact(emergencyContact);
        this.setRelationship(relationship);
        this.setAppointments(appointments);
    }

    public long getMrn() {
        return mrn;
    }

    public void setMrn(long mrn) {
        this.mrn = mrn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public EmergencyContact getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(EmergencyContact emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
