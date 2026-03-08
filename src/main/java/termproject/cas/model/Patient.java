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
    private String contactRelationship;
    private List<Address> addresses;
    private List<String> emails;
    private List<Phone> phones;

    @JsonIgnore
    private List<Appointment> appointments;

    public Patient() {}

    public Patient(
            long mrn,
            String fn,
            String mid,
            String ln,
            String sex,
            LocalDate dob) {
        this.setMrn(mrn);
        this.setFirstName(fn);
        this.setLastName(ln);
        this.setMiddleName(mid);
        this.setSex(sex);
        this.setDob(dob);
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

    public String getContactRelationship() {
        return contactRelationship;
    }

    public void setContactRelationship(String relationship) {
        if (emergencyContact != null) this.contactRelationship = relationship;
        else throw new IllegalStateException("Cannot set relationship without an emergency contact");
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }
}
