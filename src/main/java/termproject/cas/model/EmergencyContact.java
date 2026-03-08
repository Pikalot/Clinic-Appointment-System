package termproject.cas.model;

public class EmergencyContact {
    private long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;

    public EmergencyContact() {}

    public EmergencyContact(
            long id,
            String fn,
            String mid,
            String ln,
            String phone) {
        this.setId(id);
        this.setFirstName(fn);
        this.setLastName(ln);
        this.setPhone(phone);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
