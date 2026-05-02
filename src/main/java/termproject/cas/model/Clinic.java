package termproject.cas.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Clinic {
    private long clinicId;
    private String name;
    private String street;
    private String city;
    private String state;
    private String zip;

    public Clinic() {}

    public Clinic(long clinicId, String name, String street, String city, String state, String zip) {
        this.setClinicId(clinicId);
        this.setName(name);
        this.setStreet(street);
        this.setCity(city);
        this.setState(state);
        this.setZip(zip);
    }

    public long getClinicId() {
        return clinicId;
    }

    public void setClinicId(long clinicId) {
        this.clinicId = clinicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAddress() {
        return street + ", " + city + ", " + state + " " + zip;
    }

    @JsonProperty("nameAndAddress")
    public String getNameAndAddress() {
        return name.toUpperCase() + ", " + this.getAddress();
    }
}
