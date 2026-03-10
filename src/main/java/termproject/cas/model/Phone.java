package termproject.cas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Phone {
    private long id;
    private String phone;
    private String contactTime;

    @JsonIgnore
    private Patient owner;

    public Phone(){}

    public Phone(long id, String phone, String contactTime, Patient patient) {
        this.setId(id);
        this.setContactTime(contactTime);
        this.setPhone(phone);
        this.setOwner(patient);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContactTime() {
        return contactTime;
    }

    public void setContactTime(String contactTime) {
        this.contactTime = contactTime;
    }

    public Patient getOwner() {
        return owner;
    }

    public void setOwner(Patient owner) {
        this.owner = owner;
    }
}
