package termproject.cas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.List;

public class Patient {
    private Long mrn;
    private String firstName;
    private String middleName;
    private String lastName;
    private String sex;
    private LocalDate dob;
    private List<Address> addresses;
    private List<String> emails;
    private List<Phone> phones;
    private int version;

    // Emergency Contact
    private Long contactId;
    private String firstNameEC;
    private String middleNameEC;
    private String lastNameEC;
    private String phoneEC;
    private String relationship;

    @JsonIgnore
    private List<Appointment> appointments;

    public Patient() {}

    public Patient(
            Long mrn,
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

    public Long getMrn() {
        return mrn;
    }

    public void setMrn(Long mrn) {
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

    public String getContactRelationship() {
        return this.getFirstNameEC() + " "
                + this.getMiddleNameEC()
                + (this.getMiddleNameEC() == null ? "" : " ")
                + this.getLastNameEC() + ", "
                + this.getPhoneEC();
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

    public String getFirstNameEC() {
        return firstNameEC;
    }

    public void setFirstNameEC(String firstNameEC) {
        this.firstNameEC = firstNameEC;
    }

    public String getMiddleNameEC() {
        return middleNameEC;
    }

    public void setMiddleNameEC(String middleNameEC) {
        this.middleNameEC = middleNameEC;
    }

    public String getLastNameEC() {
        return lastNameEC;
    }

    public void setLastNameEC(String lastNameEC) {
        this.lastNameEC = lastNameEC;
    }

    public String getPhoneEC() {
        return phoneEC;
    }

    public void setPhoneEC(String phoneEC) {
        this.phoneEC = phoneEC;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
