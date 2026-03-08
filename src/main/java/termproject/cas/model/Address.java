package termproject.cas.model;

public class Address {
    private long id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String type;

    public Address() {}

    public Address(
            long id,
            String street,
            String city,
            String state,
            String zipCode,
            String type) {
        this.setId(id);
        this.setStreet(street);
        this.setCity(city);
        this.setState(state);
        this.setZipCode(zipCode);
        this.setType(type);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
